package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.inneral.GeoIp
import cn.revoist.lifephoton.module.funga.data.table.VisitTable
import cn.revoist.lifephoton.module.funga.tools.VisitTool
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.server.routing.RoutingCall
import kotlinx.serialization.json.Json
import org.ktorm.dsl.count
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.limit
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * @author 6hisea
 * @date  2025/7/26 13:29
 * @description: None
 */
@RouteContainer("funga","statistics")
object Statistics {

    @Route(GET)
    suspend fun getAllCountryData(call: RoutingCall) {
        // 1. 获取所有不重复的国家列表
        val countries = FungaPlugin.dataManager.useDatabase().from(VisitTable)
            .selectDistinct(VisitTable.country)
            .map {
                it[VisitTable.country].toString()
            }

        // 2. 对每个国家查询访问次数
        val result = mutableMapOf<String, Long>()
        countries.forEach { country ->
            val count = FungaPlugin.dataManager.useDatabase().from(VisitTable)
                .select(count(VisitTable.id))
                .where {
                    VisitTable.country eq country
                }
                .map {
                    it[VisitTable.id]?.toString()?.toLong() ?: 0L
                }.first()

            result[country] = count
        }

        // 3. 返回结果（假设需要响应给客户端）
        call.ok(result)
    }
    @Route(GET)
    suspend fun getCount(call: RoutingCall) {
        FungaPlugin.dataManager.useDatabase().from(VisitTable)
            .select(VisitTable.id)
            .orderBy(VisitTable.id.desc())
            .limit(1)
            .map {
                it[VisitTable.id]
            }.first()
    }
    @Route(GET)
    suspend fun getLastRecord(call: RoutingCall) {
        call.ok(
            FungaPlugin.dataManager.useDatabase().from(VisitTable)
                .select()
                .orderBy(VisitTable.id.desc())
                .limit(100)
                .map { row->
                    mapOf(
                        "ip" to row[VisitTable.ip],
                        "lat" to row[VisitTable.lat],
                        "lon" to row[VisitTable.lon],
                        "country" to row[VisitTable.country],
                        "region" to row[VisitTable.region],
                        "city" to row[VisitTable.city],
                    )
                }
        )
    }

    @Route(GET)
    suspend fun visit(call: RoutingCall){
        val ip = VisitTool.getClientIp(call)
        var geo: GeoIp? = null

        // 检查请求频率 (例如：每分钟最多5次)
        val currentTime = System.currentTimeMillis()
        val lastAccessTime = VisitTool.ipAccessCache[ip] ?: 0
        val timeDiff = currentTime - lastAccessTime

        if (timeDiff < TimeUnit.MINUTES.toMillis(1) / 5) { // 12秒内只能请求一次
            call.error("error:cooldown")
            return
        }
        try {
            // 使用Java URLConnection
            val url = URL("${VisitTool.API}$ip")
            val connection = url.openConnection()

            BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
                val jsonResponse = reader.readText()
                val geoInfo = Json.decodeFromString<GeoIp>(jsonResponse)
                geo = geoInfo
            }
        } catch (e: Exception) {
            call.error(e.toString())
        }
        if (geo != null){
            FungaPlugin.dataManager.useDatabase().insert(VisitTable){
                set(VisitTable.ip,geo.query)
                set(VisitTable.lat,geo.lat)
                set(VisitTable.lon,geo.lat)
                set(VisitTable.country,geo.country)
                set(VisitTable.region,geo.region)
                set(VisitTable.city,geo.city)
            }
            call.ok("ok")
        }else{
            call.error("error: geo not found")
        }
    }
}