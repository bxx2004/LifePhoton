package cn.revoist.lifephoton.extensions.filem.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.extensions.auth.data.Tools
import cn.revoist.lifephoton.extensions.filem.FileManagement
import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.isLogin
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.ok
import cn.revoist.lifephoton.plugin.route.receiveMultiData
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
@AutoRegister("file-management",Booster.SystemVersion.ADVANCED)
object Upload : RoutePage("upload",false,false) {
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Post)
    }

    override suspend fun onPost(call: RoutingCall) {
        val limitSize = FileManagement.option<Long>("size-limit")
        val userId = if (call.isLogin()) {
            Tools.findUserByToken((call.sessions.get("user") as UserSession).accessToken)!!.id.toString()
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
                    val code = System.currentTimeMillis().toString() + "-" + Tools.generateCode()
                    val f = File(FileManagement.option<String>("path") + "/" + userId + "-" +  code + "-" + name)
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