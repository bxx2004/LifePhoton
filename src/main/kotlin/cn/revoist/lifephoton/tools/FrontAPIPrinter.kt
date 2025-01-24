package cn.revoist.lifephoton.tools

import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.getPlugin
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.RoutePage
import io.ktor.http.*
import org.reflections.Reflections
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.reflect.jvm.kotlinFunction

/**
 * @author 6hisea
 * @date  2025/1/24 18:25
 * @description: None
 */
object FrontAPIPrinter {
    class FrontAPI(val path:String,val mName:String = path,val methods:List<String>){
        override fun toString(): String {
            var r = ""
            val header = if (mName.startsWith("get")) {
                ""
            }else{
                "request"
            }
            var pa = "("
            if ("\\{.*?}".toRegex().find(path) != null){
                "\\{.*?}".toRegex().findAll(path).forEach {
                    pa += it.value.replace("{","").replace("}","") +":string,"
                }
                pa += "params)"
                methods.forEach {
                    r+= "  async ${header}${wordToCase(mName)}$pa{\n"
                    r+= "    let res = await requests.${it}WithResponse(`${path}`,params)\n"
                    r+= "    return res.payload\n"
                    r+= "  }\n"
                }
            }else{
                methods.forEach {
                    r+= "  async ${header}${wordToCase(mName)}(params){\n"
                    r+= "    let res = await requests.${it}WithResponse(\"${path}\",params)\n"
                    r+= "    return res.payload\n"
                    r+= "  }\n"
                }
            }
            return r
        }
    }

    val result = HashMap<String,ArrayList<FrontAPI>>()
    fun generate(outPath:String){
        println("生产中···")
        val ref = Reflections("cn.revoist.lifephoton")
        //单页路由查找
        for (clazz in ref.getTypesAnnotatedWith(AutoRegister::class.java)) {
            clazz.getAnnotation(AutoRegister::class.java)?.let {
                val obj = clazz.getField("INSTANCE").get(null)
                if (obj is RoutePage){
                    val api = FrontAPI(
                        "/" + it.pluginId + "/" + obj.path,
                        clazz.simpleName,
                        obj.methods().map { it.value.lowercase() }
                    )
                    if (result[it.pluginId] == null){
                        result[it.pluginId] = arrayListOf()
                    }
                    result[it.pluginId]?.add(api)
                }
            }
        }
        //查找容器
        for (clazz in ref.getTypesAnnotatedWith(RouteContainer::class.java)) {
            clazz.getAnnotation(RouteContainer::class.java)?.let { gateway->
                clazz.declaredMethods.filter {
                    it.kotlinFunction?.annotations?.filterIsInstance<Route>()?.first() != null
                }.forEach {
                    val route = it.kotlinFunction!!.annotations.filterIsInstance<Route>().first()
                    val path = if (route.path == "&empty"){
                        formatConversion(it.name)
                    }else{
                        route.path
                    }

                    val api = FrontAPI(
                        "/" + gateway.pluginId + "/" + gateway.root + "/" + path,
                        path,
                        arrayListOf(route.method.lowercase())
                    )
                    if (result[gateway.pluginId] == null){
                        result[gateway.pluginId] = arrayListOf()
                    }
                    result[gateway.pluginId]?.add(api)

                }
            }
        }

        result.forEach { t, u ->
            val f= File("$outPath/$t.ts")
            if (!f.exists()) {
                f.createNewFile()
            }
            var r = "import requests from \"../index.ts\";\n"
            r += "export default {\n"
            r += u.joinToString("\n") { it.toString() }
            r += "}\n"
            f.writeText(r)
            println("生产 ${t} 完毕···")
        }
        println("生产完成···")

    }
    private fun formatConversion(str:String):String {
        var result = ""
        for (char in str) {
            if (char.isUpperCase()){
                result += "-${char.lowercase()}"
            }else{
                result += char
            }
        }
        return result
    }
    fun wordToCase(v:String):String{
        var r = ""
        var a = false
        for (c in v) {
            if (c == '-' || c == '/'){
                a = true
                continue
            }else{
                if (a){
                    r += c.uppercase()
                    a = false
                }else{
                    r += c
                }
            }
        }
        return r
    }
}

fun main() {
    FrontAPIPrinter.generate("C:/Users/12232/Desktop")
}