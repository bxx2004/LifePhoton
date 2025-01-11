package cn.revoist.lifephoton.plugin.event.events

/**
 * @path cn.revoist.lifephoton.plugin.event.events.RootPageRequestEvent
 * @author 6hisea
 * @date  2025/1/7 18:49
 * @description: None
 */
data class RootPageRequestEvent(override var path:String, var content:Any) :PageRequestEvent(path,null){
}