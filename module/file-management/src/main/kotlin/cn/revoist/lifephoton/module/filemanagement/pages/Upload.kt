package cn.revoist.lifephoton.module.filemanagement.pages

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.module.filemanagement.FileManagement
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.io.File

/**
 * @author 6hisea
 * @date  2025/1/10 12:24
 * @description: None
 */
@AutoRegister("file-management")
object Upload : RoutePage("upload",false,false) {
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Post)
    }

    override suspend fun onPost(call: RoutingCall) {
        val limitSize = 1073741824
        val userId = if (call.isLogin()) {
            cn.revoist.lifephoton.module.authentication.data.Tools.findUserByToken((call.sessions.get("user") as UserSession).accessToken)!!.id.toString()
        }else{
            "default"
        }
        call.receiveMultipart().forEachPart { part ->
            if (part is PartData.FileItem) {
                val name = part.originalFileName
                val file = part.provider().readBuffer().readByteArray()
                if (file.size > limitSize){
                    call.error("limit size: <= $limitSize")
                    return@forEachPart
                }else{
                    val code = System.currentTimeMillis().toString() + "-" + cn.revoist.lifephoton.module.authentication.data.Tools.generateCode()
                    val f = File(FileManagement.workdir.absolutePath + "/" + userId + "-" +  code + "-" + name)
                    if (f.exists()){
                        f.delete()
                    }
                    f.createNewFile()
                    f.writeBytes(file)
                    call.ok(
                        hashMapOf(
                            "path" to code
                        )
                    )
                }
            }
        }
    }
}