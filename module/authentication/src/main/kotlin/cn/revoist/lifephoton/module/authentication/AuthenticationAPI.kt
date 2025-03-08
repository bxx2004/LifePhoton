package cn.revoist.lifephoton.module.authentication

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.module.authentication.data.Tools
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * @author 6hisea
 * @date  2025/3/4 20:28
 * @description: None
 */
suspend fun RoutingCall.isLogin():Boolean{
    val userCookie = sessions.get("user") ?: return false
    val event = AuthenticationEvent(userCookie as UserSession,false).call() as AuthenticationEvent
    return event.truth
}
suspend fun RoutingCall.getUser():UserSession{
    val userCookie = sessions.get("user")
    if (userCookie != null && userCookie is UserSession) {
        return userCookie
    }
    throw RuntimeException("not user")
}
val UserSession.asEntity:UserDataEntity?
    get() = Tools.findUserByToken(accessToken)