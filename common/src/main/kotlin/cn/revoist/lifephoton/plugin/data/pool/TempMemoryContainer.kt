package cn.revoist.lifephoton.plugin.data.pool

import cn.revoist.lifephoton.plugin.Plugin

/**
 * @author 6hisea
 * @date  2025/4/22 18:26
 * @description: None
 */
class TempMemoryContainer<T>() {
    private val pool = hashMapOf<String,T>()

    fun destroy(){
        pool.clear()
    }
    fun createMemory(id:String,data:T){
        if (pool.containsKey(id)){
            return
        }
        pool[id] = data
    }

    fun callMemory(id:String,default:T):T{
        if (!exists(id)){
            pool[id] = default
        }
        return pool[id]!!
    }
    fun exists(id:String):Boolean{
        return pool.containsKey(id)
    }

}