package cn.revoist.lifephoton.plugin.event.events

import cn.revoist.lifephoton.plugin.event.Event
import io.ktor.server.application.*

/**
 * @path cn.revoist.lifephoton.plugin.event.events.ConfiguerEvent
 * @author 6hisea
 * @date  2025/1/7 19:07
 * @description: None
 */
data class ConfigureEvent(val app:Application): Event() {
}