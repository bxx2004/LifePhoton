package cn.revoist.lifephoton.module.authentication.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.module.authentication.data.Tools
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.message
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * @author 6hisea
 * @date  2025/1/10 12:37
 * @description: None
 */
@AutoRegister("auth")
object Logout : RoutePage("logout",true,false){
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Get)
    }

    override suspend fun onGet(call: RoutingCall) {
        val session = (call.sessions.get("user")?:UserSession("-1","-1")) as UserSession
        if (Tools.checkToken(session)){
            Tools.updateToken(Tools.findUserByToken(session.accessToken)!!.username)
            call.sessions.set("user",null)
            call.message("login successful")
        }else{
            call.error("Not login")
        }
    }
}