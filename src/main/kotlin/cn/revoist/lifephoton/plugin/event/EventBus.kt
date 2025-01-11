package cn.revoist.lifephoton.plugin.event

/**
 * @path cn.revoist.lifephoton.plugin.event.EventBus
 * @author 6hisea
 * @date  2025/1/7 18:22
 * @description: None
 */
object EventBus {
    val listeners =ArrayList<Listener>()
    fun call(event: Event){
        listeners.sortBy { it.priority.num }
        for (listener in listeners) {
            if (listener.listen.name == event::class.java.name){
                listener.onCalled(event)
            }
        }
    }
}