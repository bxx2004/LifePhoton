package cn.revoist.lifephoton.plugin.event

/**
 * @path cn.revoist.lifephoton.plugin.event.Listener
 * @author 6hisea
 * @date  2025/1/7 18:24
 * @description: None
 */
open class Listener {
    lateinit var listen:Class<out Event>
    var priority = Priorities.DEFAULT
    fun configure(func:Listener.()->Unit):Listener {
        func(this)
        return this
    }
    open fun onCalled(event: Event) : Listener {
        return this
    }
    fun register(){
        EventBus.listeners.add(this)
    }
}
enum class Priorities(val num: Int) {
    LOW(-1),
    DEFAULT(0),
    HIGH(1)
}
fun <T:Event>registerListener(event:Class<T>,func:(event:T)->Unit):Listener{
    val listener = object : Listener(){
        override fun onCalled(event: Event): Listener {
            func(event as T)
            return super.onCalled(event)
        }
    }
    listener.configure {
        listen = event
    }
    listener.register()
    return listener
}