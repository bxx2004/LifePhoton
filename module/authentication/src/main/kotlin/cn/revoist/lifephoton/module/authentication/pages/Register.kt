package cn.revoist.lifephoton.module.authentication.pages

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.module.authentication.data.entity.request.RegisterRequest
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.message
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/1/8 18:46
 * @description: None
 */
@AutoRegister("auth")
object Register : RoutePage("register",false,false){
    val emailCodeCache = HashMap<String, String>()
    override fun methods(): List<HttpMethod> {
        return listOf(HttpMethod.Post)
    }

    override suspend fun onPost(call: RoutingCall) {
        val request = call.receive<RegisterRequest>()
        emailCodeCache.remove(request.email)
        if (emailCodeCache[request.email] == request.emailCode) {
            if (!cn.revoist.lifephoton.module.authentication.data.Tools.hasUser(request.username,request.email)){
                val user = UserDataEntity {
                    this.email = request.email
                    this.username = request.username
                    this.password = request.password
                    this.group = "default"
                    this.permissions = arrayListOf("default.*")
                    this.data = hashMapOf(
                        "company" to request.company,
                        "description" to request.description
                    )
                    this.avatar = request.avatar
                }
                cn.revoist.lifephoton.module.authentication.data.Tools.addUser(user)
                call.message("user registered successfully")
            }else{
                call.error("user is exist")
            }
        }else{
            call.error("email code is invalid")
        }
    }
}