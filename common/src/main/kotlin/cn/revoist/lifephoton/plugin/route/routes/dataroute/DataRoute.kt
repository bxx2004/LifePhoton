package cn.revoist.lifephoton.plugin.route.routes.dataroute

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.data.pool.DynamicPage
import cn.revoist.lifephoton.plugin.data.pool.DynamicPageInformation
import cn.revoist.lifephoton.plugin.route.Route
import io.ktor.server.routing.RoutingCall
import kotlinx.css.table
import org.ktorm.dsl.from
import org.ktorm.schema.Table

/**
 * @author 6hisea
 * @date  2025/8/3 17:33
 * @description: None
 */
abstract class DataRoute(val plugin: Plugin,val table: Table<*>) {
    @Route
    suspend fun select(call: RoutingCall){
        plugin.dataManager.useDynamicPagination { pagination, size ->
            DynamicPageInformation(
                arrayListOf(),1,1
            )
        }
    }
}