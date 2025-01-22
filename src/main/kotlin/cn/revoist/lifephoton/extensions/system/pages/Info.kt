package cn.revoist.lifephoton.extensions.system.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.http.*
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/1/18 15:51
 * @description: None
 */
@AutoRegister("system")
object Info :RoutePage("info",false,false) {
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Get)
    }

    override suspend fun onGet(call: RoutingCall) {
        call.ok(Setting.INS)
    }
    class Setting{
        companion object{
            val INS = Setting()
        }
        val version = Booster.SYSTEM_VERSION.num
        val views = Booster.SYSTEM_VERSION.views
    }
}