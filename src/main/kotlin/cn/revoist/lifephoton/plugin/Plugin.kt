package cn.revoist.lifephoton.plugin

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.ktors.hasPlugin
import cn.revoist.lifephoton.plugin.data.DataManager
import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import cn.revoist.lifephoton.plugin.event.events.PluginPageRequestEvent
import cn.revoist.lifephoton.plugin.route.ErrorResponse
import cn.revoist.lifephoton.plugin.route.RoutePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.logging.*

/**
 * @path cn.revoist.lifephoton.plugin.Plugin
 * @author 6hisea
 * @date  2025/1/7 18:14
 * @description: None
 */
abstract class Plugin {
    private lateinit var application: Application
    lateinit var logger: Logger
    abstract val name:String
    abstract val author:String
    abstract val version:String
    private val config = HashMap<String,Any>()
    open val id:String
        get() = name.lowercase()
    abstract fun load()
    abstract fun configure()

    fun <T>option(key:String):T{
        return config[key] as T
    }
    fun <T>option(key:String,d:T):T{
        return (config[key]?:d) as T
    }
    fun optional(key: String,value:Any){
        config[key] = value
    }


    val dataManager = DataManager(this)

    fun setApplication(application: Application) {
        this.application = application
    }

    fun registerPage(
        page:RoutePage
    ){
        page.methods().forEach {
            registerRoute(it,page.path,page.auth,page.injectable){
                if (it == HttpMethod.Get){
                    page.onGet(call)
                }else if (it == HttpMethod.Post){
                    page.onPost(call)
                }else{
                    page.onRequest(it,call)
                }
            }
        }

    }

    suspend fun isLogin(call:RoutingCall):Boolean{
        val userCookie = call.sessions.get("user") ?: return false
        val event = AuthenticationEvent(userCookie as UserSession,false).call() as AuthenticationEvent
        return event.truth
    }

    fun registerRoute(
        method: HttpMethod,
        path: String,
        auth: Boolean = false,
        injectable: Boolean = false,
        func: suspend RoutingContext.() -> Unit
    ){
        application.routing {
            if (auth && hasPlugin("auth")){
                authenticate("auth-session") {
                    when(method){
                        HttpMethod.Post->{
                            post("$id/$path"){
                                if (injectable){
                                    val event = PluginPageRequestEvent("$id/$path",call,name).call() as PluginPageRequestEvent
                                    if (!event.isCancelled){
                                        func(this)
                                    }
                                }else{
                                    func(this)
                                }
                            }
                        }
                        HttpMethod.Get->{
                            get("$id/$path"){
                                if (injectable){
                                    val event = PluginPageRequestEvent("$id/$path",call,name).call() as PluginPageRequestEvent
                                    if (!event.isCancelled){
                                        func(this)
                                    }
                                }else{
                                    func(this)
                                }
                            }
                        }
                    }
                }
            }else{
                when(method){
                    HttpMethod.Post->{
                        post("$id/$path"){
                            if (injectable){
                                val event = PluginPageRequestEvent("$id/$path",call,name).call() as PluginPageRequestEvent
                                if (!event.isCancelled){
                                    func(this)
                                }
                            }else{
                                func(this)
                            }
                        }
                    }
                    HttpMethod.Get->{
                        get("$id/$path"){
                            if (injectable){
                                val event = PluginPageRequestEvent("$id/$path",call,name).call() as PluginPageRequestEvent
                                if (!event.isCancelled){
                                    func(this)
                                }
                            }else{
                                func(this)
                            }
                        }
                    }
                }
            }

        }
    }
}