package cn.revoist.lifephoton.module.funga.tools

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import java.util.concurrent.ConcurrentHashMap
import kotlin.text.split

/**
 * @author 6hisea
 * @date  2025/7/26 14:40
 * @description: None
 */
object VisitTool {
    val ipAccessCache = ConcurrentHashMap<String, Long>()
    const val API = "http://ip-api.com/json/"

    fun getClientIp(call: ApplicationCall): String {
        return call.request.headers["X-Forwarded-For"]?.split(",")?.first()?.trim()
            ?: call.request.origin.remoteHost
    }
}