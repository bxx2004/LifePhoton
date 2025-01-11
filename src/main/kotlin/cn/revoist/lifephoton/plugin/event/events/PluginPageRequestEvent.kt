package cn.revoist.lifephoton.plugin.event.events

import io.ktor.server.routing.*

/**
 * @path cn.revoist.lifephoton.plugin.event.events.PluginPageRequestEvent
 * @author 6hisea
 * @date  2025/1/7 19:24
 * @description: None
 */
class PluginPageRequestEvent(override var path:String, override val call:RoutingCall
,val name:String
) : PageRequestEvent(path,call) {
}