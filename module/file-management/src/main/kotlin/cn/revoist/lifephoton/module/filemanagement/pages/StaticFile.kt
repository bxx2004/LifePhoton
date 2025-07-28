package cn.revoist.lifephoton.module.filemanagement.pages

import cn.revoist.lifephoton.module.filemanagement.FileManagement
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

/**
 * @author 6hisea
 * @date  2025/4/3 20:50
 * @description: None
 */
@AutoRegister("file-management")
object StaticFile : RoutePage("static"){
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Get)
    }

    override suspend fun onGet(call: RoutingCall) {
        val path = call.queryParameters["path"]
        if (path == null) {
            call.error("Please input the path.")
            return
        }else{
            val file = File(FileManagement.workdir.absolutePath + "/static/"+path)
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, file.name)
                    .toString()
            )
            call.respondFile(file)
        }
    }
}