package cn.revoist.lifephoton.extensions.auth.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.extensions.auth.data.Tools
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.data.toPayloadResponse
import cn.revoist.lifephoton.plugin.route.RoutePage
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/1/9 10:47
 * @description: None
 */
@AutoRegister("auth",Booster.SystemVersion.ADVANCED)
object Profile :RoutePage("profile/{user}",true,true){
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Get)
    }

    override suspend fun onGet(call: RoutingCall) {
        val username = call.parameters["user"]
        if (username.isNullOrEmpty()){
            call.error("You must enter a username")
            return
        }
        val userEntity = Tools.getUser(username)?.toPayloadResponse(
            excludes = arrayListOf("password","accessToken","refreshToken","permissions")
        )
        if (userEntity == null){
            call.error("user is not exists")
        }else{
            call.respond(userEntity)
        }
    }
}