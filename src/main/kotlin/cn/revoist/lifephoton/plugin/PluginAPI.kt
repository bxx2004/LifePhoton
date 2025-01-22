package cn.revoist.lifephoton.plugin

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

private val plugins = ArrayList<Plugin>()
private lateinit var initServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
/**
 * @author 6hisea
 * @date  2025/1/11 20:23
 * @description: None
 */
interface PluginAPI {
    val plugin:Plugin

}
fun hasPlugin(id:String):Boolean{
    return plugins.any { it.id == id }
}

fun getPlugin(id:String):Plugin?{
    return plugins.find { it.id == id }
}

fun usePlugin(plugin: Plugin) {
    plugin.setApplication(initServer.application)
    plugin.logger = initServer.application.log
    plugins.add(plugin)
    plugin.configure()
    plugin.load()
}
fun getPlugins():List<Plugin>{
    return plugins
}
fun initPluginProvider(server:EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>){
    initServer = server
}
suspend fun RoutingCall.isLogin():Boolean{
    val userCookie = sessions.get("user") ?: return false
    val event = AuthenticationEvent(userCookie as UserSession,false).call() as AuthenticationEvent
    return event.truth
}
fun <T:PluginAPI>pluginApi(pluginId:String):T?{
    return getPlugin(pluginId)?.api as T?
}