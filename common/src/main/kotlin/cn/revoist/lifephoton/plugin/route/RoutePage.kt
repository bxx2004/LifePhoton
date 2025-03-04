package cn.revoist.lifephoton.plugin.route

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/1/8 12:49
 * @description: None
 */
abstract class RoutePage(val path:String, val auth:Boolean = false, val injectable:Boolean = false) {
    open suspend fun onRequest(method:HttpMethod,call:RoutingCall){}
    open suspend fun onGet(call: RoutingCall){}
    open suspend fun onPost(call:RoutingCall){}
    abstract fun methods():List<HttpMethod>
}
 suspend fun <T>RoutingCall.ok(data:T,message:String = "Successful"){
    if (data is Response){
        respond(data)
        return
    }
    respond(
        PayloadResponse(true,message,data)
    )
}
 suspend fun RoutingCall.message(message:String = "Successful"){
    respond(
        MessageResponse(true,message)
    )
}
 suspend fun RoutingCall.error(message:String = "Failed"){
    respond(
        ErrorResponse(false,message)
    )
}
 suspend fun RoutingCall.empty(message:String = "Empty"){
    respond(
        ErrorResponse(false,message)
    )
}
suspend inline fun RoutingCall.checkParameters(vararg params:String){
    for (param in params) {
        if (!queryParameters.contains(param) || queryParameters[param] == null){
            error("Missing URL parameters: $param")
            return
        }
    }
}