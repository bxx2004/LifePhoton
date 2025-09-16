package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.authentication.asEntity
import cn.revoist.lifephoton.module.authentication.getUser
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.module.funga.services.BigDataService
import cn.revoist.lifephoton.plugin.match
import cn.revoist.lifephoton.plugin.route.Api
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.Param
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.checkParameters
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.server.routing.RoutingCall

/**
 * @author 6hisea
 * @date  2025/8/27 14:16
 * @description: None
 */
@RouteContainer("funga","bigdata")
object BigData {
    @Route(GET)
    suspend fun next(call: RoutingCall){
        call.match {
            isLogin()
        }.then {
            val user = call.getUser().asEntity!!
            if (user.permissions.contains("bigdata.use")){
                if (BigDataService.hasNext()){
                    val data = BigDataService.next(user.id)
                    if (data.keys.isEmpty()){
                        error("There is no data at this time")
                    }else{
                        ok(data)
                    }
                }else{
                    error("There is no data at this time")
                }
            }else{
                error("You don't have permission to use this function!")
            }
        }.default {
            error("Please login")
        }
    }
    @Route(GET)
    @Api(
        description = "标记当前数据的状态，更新余额",
        params = [
            Param("id:string","数据ID"),
            Param("state:boolean","状态")
        ]
    )
    suspend fun mark(call: RoutingCall){
        call.checkParameters("id","state")
        val id = call.queryParameters["id"]!!.toLong()
        val state = call.request.queryParameters["state"]!!.toBoolean()
        call.match {
            isLogin()
        }.then {
            val user = call.getUser().asEntity!!
            if (user.permissions.contains("bigdata.use")){
                BigDataService.updateWallet(user.id,BigDataService.mark(id,state,user.id))
            }else{
                error("You don't have permission to use this function!")
            }
        }.default {
            error("Please login")
        }
    }
    @Route(GET)
    suspend fun wallet(call: RoutingCall){
        call.match {
            isLogin()
        }.then {
            val user = call.getUser().asEntity!!
            if (user.permissions.contains("bigdata.use")){
                ok(
                    mapOf(
                        "point" to BigDataService.wallet(user.id),
                        "log" to BigDataService.walletLog(user.id)
                    )
                )
            }else{
                error("You don't have permission to use this function!")
            }
        }.default {
            error("Please login")
        }
    }
}