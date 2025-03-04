package cn.revoist.lifephoton.plugin.data.pool

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.tools.submit

/**
 * @author 6hisea
 * @date  2025/1/9 14:20
 * @description: None
 */
class BufferPool(val plugin:Plugin,val id:String) : HashMap<String,Any> () {
    init {
        setClearTask(1000 * 60 *60*3)
    }
    private fun setClearTask(time:Int){
        submit(-1,time){
            clear()
        }
    }
}