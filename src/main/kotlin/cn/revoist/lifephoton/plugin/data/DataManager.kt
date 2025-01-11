package cn.revoist.lifephoton.plugin.data

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.extensions.auth.data.Tools
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.data.pool.BufferPool
import cn.revoist.lifephoton.plugin.data.pool.Page
import cn.revoist.lifephoton.plugin.data.pool.splitPage
import cn.revoist.lifephoton.plugin.route.PagingPayloadResponse
import cn.revoist.lifephoton.plugin.route.PayloadResponse
import cn.revoist.lifephoton.tools.submit
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.schema.Table

/**
 * @author 6hisea
 * @date  2025/1/8 10:59
 * @description: None
 */

class DataManager(val plugin:Plugin) {
    private val databases = ArrayList<Database>()
    private val bufferPool = BufferPool(plugin,plugin.id)
    private val pageBuffer = HashMap<String,List<Page>>()
    fun useDatabase(dbName:String = plugin.id):Database{
        if (databases.map { it.name }.contains(dbName)) {
            return databases.find { it.name == dbName }!!
        }
        val db = Database.connect("jdbc:postgresql://${Booster.DB_URL}/${dbName}","org.postgresql.Driver",Booster.DB_USERNAME,Booster.DB_PASSWORD)
        databases.add(db)
        return db
    }
    fun useDefaultDatabase():Database{
        return Booster.database
    }
    fun useTable(table:Table<*>,dbName:String = plugin.id){
        val db = databases.find { it.name == dbName }
        if (db == null) {
            throw RuntimeException("Not found database $dbName")
        }
        //表不存在的话自动创建
    }
    fun useBuffers():BufferPool{
        return bufferPool
    }
    private fun useBuffer(key:String, id:String = "default"):Any?{
        return bufferPool["$id:$key"]
    }
    fun useBuffer(key: String,id: String ="default",defaultFunc:()->Any):Any{
        return useBuffer(key,id)?:defaultFunc()
    }
    fun useBuffer(keyWithId:String,defaultFunc:()->Any):Any{
        val r = useBuffer(keyWithId.split(":")[1],keyWithId.split(":")[0])
        if (r == null){
            bufferPool[keyWithId] = defaultFunc()
        }
        return bufferPool[keyWithId]!!
    }
    fun useBuffer(keyWithId:String,timeout:Int = 1000*60,defaultFunc:()->Any):Any{
        submit(timeout,-1){
            bufferPool.remove(keyWithId)
            it.cancel()
        }
        return useBuffer(keyWithId, defaultFunc)
    }
    fun usePagination(data:List<Any>,count:Int = 20,lock:Boolean = false):PagingPayloadResponse<*>{
        val code = Tools.generateCode()
        val pages = splitPage(data,count,"${plugin.id}-${code}",lock)
        pageBuffer[code] = pages
        return pages[0].toResponse()
    }
    fun getPage(code:String,num:Int):Page?{
        val page = pageBuffer[code]
        if (page != null){
            if (num >= 1 && num <= page.size){
                return page.find { it.pagination == num }
            }
        }
        return null
    }
}
fun Entity<*>.toPayloadResponse(message:String = "success",excludes:List<String> = arrayListOf(),joins:HashMap<String,Any?> = hashMapOf()):PayloadResponse<HashMap<String,Any?>>{
    val payload = HashMap<String,Any?>()
    this.properties.forEach { (t, u) ->
        if (!excludes.contains(t)){
            payload[t] = u
        }
    }
    joins.forEach { (t, u) ->
        payload[t] = u
    }
    return PayloadResponse(
        true,message,payload
    )
}