package cn.revoist.lifephoton.extensions.filem.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.extensions.filem.FileManagement
import cn.revoist.lifephoton.extensions.filem.data.table.FileMetaTable
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.receiveMultiData
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.ktorm.dsl.from

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
        val limitSize = if (FileManagement.isLogin(call)){
            FileManagement.option<Long>("size-limit")
        }else{
            FileManagement.option<Long>("no-login-size-limit")
        }

        call.receiveMultiData {
            val file = it["file"] as ByteArray
            if (file.size > limitSize){
                call.error("limit size: <= $limitSize")
            }else{

            }
        }

    }
}