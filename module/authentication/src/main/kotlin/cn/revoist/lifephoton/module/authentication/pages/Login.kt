package cn.revoist.lifephoton.module.authentication.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.module.authentication.data.entity.request.LoginRequest
import cn.revoist.lifephoton.module.authentication.data.entity.response.LoginResponse
import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.empty
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * @author 6hisea
 * @date  2025/1/8 13:10
 * @description: None
 */
@AutoRegister("auth")
object Login : RoutePage("login",false,false){
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Post)
    }

    override suspend fun onPost(call: RoutingCall) {
        val request = call.receive<LoginRequest>()
        if (cn.revoist.lifephoton.module.authentication.data.Tools.comparePassword(request.username,request.password)){
            val tokens = cn.revoist.lifephoton.module.authentication.data.Tools.updateToken(request.username)
            call.sessions.set(UserSession(tokens.first,tokens.second))
            call.ok(
                LoginResponse(
                    tokens.first,
                    tokens.second
                )
            )
        }else{
            call.empty("The user does not exist or the password is incorrect")
        }
    }
}