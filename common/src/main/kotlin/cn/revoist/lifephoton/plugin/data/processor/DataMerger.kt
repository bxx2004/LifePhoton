package cn.revoist.lifephoton.plugin.data.processor


fun <T>join(keys:List<String>,generator:(key:String)->T):List<MergeData<T>>{
    val map = ArrayList<MergeData<T>>()
    keys.forEach {
        map.add(MergeData(it, generator(it)))
    }
    return map
}
data class MergeData<T>(val database:String, var data:T)
