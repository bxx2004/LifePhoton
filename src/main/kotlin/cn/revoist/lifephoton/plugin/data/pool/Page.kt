package cn.revoist.lifephoton.plugin.data.pool

import cn.revoist.lifephoton.plugin.route.PagingPayloadResponse
import java.util.UUID
import kotlin.math.ceil

/**
 * @author 6hisea
 * @date  2025/1/9 15:49
 * @description: None
 */
data class Page(val pagination:Int,val allPagination:Int,val data:List<Any?>,val session:String,var lock:Boolean){
    fun toResponse():PagingPayloadResponse<*>{
        val pre = if (pagination != 1){
            pagination - 1
        }else{
            -1
        }
        val next = if (pagination >= allPagination){
            -1
        }else{
            pagination +1
        }
        return PagingPayloadResponse(true,"success",pre,next,allPagination,session,data)
    }
}
fun splitPage(data: List<Any?>, count:Int,session: String,lock:Boolean):List<Page> {
    val result = ArrayList<Page>()
    val allPage = ceil((data.size.toDouble() / count.toDouble())).toInt()
    for (i in 0 until allPage) {
        if (data.size <= i*count+count){
            result.add(Page(i+1,allPage,data.subList(i*count,data.size),session,lock))
        }else{
            result.add(Page(i+1,allPage, data.subList(i*count,i*count+count),session,lock))
        }
    }
    return result
}