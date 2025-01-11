package cn.revoist.lifephoton.plugin.route

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/1/8 12:49
 * @description: None
 */
abstract class RoutePage(val path:String, val auth:Boolean, val injectable:Boolean) {
    open suspend fun onRequest(method:HttpMethod,call:RoutingCall){}
    open suspend fun onGet(call: RoutingCall){}
    open suspend fun onPost(call:RoutingCall){}
    abstract fun methods():List<HttpMethod>
    protected suspend fun <T>RoutingCall.ok(data:T,message:String = "Successful"){
        respond(
            PayloadResponse(true,message,data)
        )
    }
    protected suspend fun RoutingCall.message(message:String = "Successful"){
        respond(
            MessageResponse(true,message)
        )
    }
    protected suspend fun RoutingCall.error(message:String = "Failed"){
        respond(
            ErrorResponse(false,message)
        )
    }
    protected suspend fun RoutingCall.empty(message:String = "Empty"){
        respond(
            ErrorResponse(false,message)
        )
    }
}