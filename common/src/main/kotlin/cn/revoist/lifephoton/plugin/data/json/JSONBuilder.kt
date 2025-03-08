package cn.revoist.lifephoton.plugin.data.json

/**
 * @author 6hisea
 * @date  2025/3/5 14:11
 * @description: None
 */
class ObjBuilder {
    private val ele = JSONObject()
    fun put(key: String, value: Any) {
        ele[key] = value
    }
    fun obj(key: String,func: ObjBuilder.() -> Unit) {
        ele[key] = jsonObject {
            func(this)
        }
    }
    fun array(key: String,func: ArrayBuilder.() -> Unit) {
        ele[key] = jsonArray {
            func(this)
        }
    }
    fun build(): JSONObject {
        return ele
    }
}
class ArrayBuilder {
    private val ele = JSONArray()
    fun add(value: Any) {
        ele.add(value)
    }
    fun build(): JSONArray {
        return ele
    }
}
fun jsonObject(func:ObjBuilder.()->Unit):JSONObject{
    val builder = ObjBuilder()
    func(builder)
    return builder.build()
}
fun jsonArray(func:ArrayBuilder.()->Unit):JSONArray{
    val builder = ArrayBuilder()
    func(builder)
    return builder.build()
}