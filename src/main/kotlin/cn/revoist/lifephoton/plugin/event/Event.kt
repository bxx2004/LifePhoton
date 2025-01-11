package cn.revoist.lifephoton.plugin.event

/**
 * @path cn.revoist.lifephoton.plugin.event.Event
 * @author 6hisea
 * @date  2025/1/7 18:22
 * @description: None
 */
abstract class Event {
    var isCancelled: Boolean = false
    fun call():Event{
        EventBus.call(this)
        return this
    }
}