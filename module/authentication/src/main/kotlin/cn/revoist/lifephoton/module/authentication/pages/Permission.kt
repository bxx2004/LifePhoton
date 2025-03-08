package cn.revoist.lifephoton.module.authentication.pages

import cn.revoist.lifephoton.module.authentication.asEntity
import cn.revoist.lifephoton.module.authentication.getUser
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.plugin.match
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/3/4 20:25
 * @description: None
 */
@RouteContainer("auth","permission")
object Permission {
    @Route(GET)
    @Api("获取当前登录用户的权限列表")
    suspend fun getPermissionList(call:RoutingCall) {
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity
            ok(user?.permissions?:ArrayList())
        }.default {
            error("未登录")
        }
    }
    @Route(GET)
    @Api("判断当前用户是否拥有某个权限")
    suspend fun hasPermission(call:RoutingCall) {
        val permission = call.queryParameters["permission"]
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            ok(user.permissions.contains(permission))
        }.default {
            error("未登录")
        }
    }
}