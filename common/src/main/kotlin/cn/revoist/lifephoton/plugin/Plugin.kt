package cn.revoist.lifephoton.plugin

import cn.revoist.lifephoton.plugin.data.DataManager
import cn.revoist.lifephoton.plugin.event.events.PluginPageRequestEvent
import cn.revoist.lifephoton.plugin.route.RoutePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import java.util.Properties
import kotlin.collections.set

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
    var properties = Properties()
    open val id:String
        get() = name.lowercase()
    abstract fun load()
    @Deprecated("已过时")
    abstract fun configure()
    @Deprecated("已过时")
    fun <T>option(key:String):T{
        return config[key] as T
    }
    @Deprecated("已过时")
    fun <T>option(key:String,d:T):T{
        return (config[key]?:d) as T
    }
    @Deprecated("已过时")
    fun optional(key: String,value:Any){
        config[key] = value
    }

    fun loadConfig(){
        properties = loadConfig(id)
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


    enum class OS {
        WINDOWS, LINUX, MAC, SOLARIS
    }

    fun getOS(): OS? {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> {
                OS.WINDOWS
            }
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                OS.LINUX
            }
            os.contains("mac") -> {
                OS.MAC
            }
            os.contains("sunos") -> {
                OS.SOLARIS
            }
            else -> null
        }
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