package cn.revoist.lifephoton.tools

import cn.revoist.lifephoton.plugin.properties
import cn.revoist.lifephoton.plugin.route.error
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/3/2 20:46
 * @description: None
 */
suspend inline fun RoutingCall.checkNotNull(vararg datas:Any?){
    if (!datas.all { it != null }){
        error("Parameter not null.")
        return
    }
}
suspend inline fun RoutingCall.checkRequest(requestBody:Any){
    for (property in requestBody.properties()) {
        if (property.get(requestBody) == null){
            error("Parameter ${property.name} must be not null.")
            return
        }
    }
}