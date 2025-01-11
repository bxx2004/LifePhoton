package cn.revoist.lifephoton.plugin.event.events

import cn.revoist.lifephoton.plugin.event.Event
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * @path cn.revoist.lifephoton.plugin.event.events.PageRequestEvent
 * @author 6hisea
 * @date  2025/1/7 18:48
 * @description: None
 */
abstract class PageRequestEvent(open var path:String,open val call: RoutingCall?) : Event() {

}