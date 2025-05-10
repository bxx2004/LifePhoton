package cn.revoist.lifephoton.module.filemanagement.pages

import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
/**
 * @author 6hisea
 * @date  2025/1/22 12:30
 * @description: None
 */
@AutoRegister("file-management")
object View : RoutePage("view"){
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Get)
    }

    override suspend fun onGet(call: RoutingCall) {
        val id = call.queryParameters["id"]
        if (id == null) {
            call.error("id must not be null!")
            return
        }
        val target = FileManagementAPI.findFileByIdentifier(id)
        if (target == null) {
            call.error("no target found for $id!")
            return
        }
        val meta = target.name.split("-")
        if (meta[0] == "default"){
            call.respondFile(target)
            return
        }
    }
}