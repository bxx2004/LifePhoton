package cn.revoist.lifephoton

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.anno.AutoUse
import cn.revoist.lifephoton.plugin.getPlugin
import cn.revoist.lifephoton.plugin.route.Gateway
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RoutePage
import cn.revoist.lifephoton.plugin.usePlugin
import io.ktor.http.*
import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.delivery.mailerBuilder
import org.ktorm.database.Database
import org.reflections.Reflections
import kotlin.coroutines.Continuation
import kotlin.reflect.jvm.kotlinFunction

/**
 * @path cn.revoist.lifephoton.configure.Booter
 * @author 6hisea
 * @date  2025/1/7 20:08
 * @description: None
 */
object Booster {


    const val VERSION = "beta-1"
    val SYSTEM_VERSION = SystemVersion.AI
    const val DB_URL = "127.0.0.1:5432"
    const val DB_NAME = "lifephoton"
    const val DB_USERNAME = "postgres"
    const val DB_PASSWORD = "123456"

    val database = Database.connect("jdbc:postgresql://${DB_URL}/${DB_NAME}","org.postgresql.Driver",
        DB_USERNAME, DB_PASSWORD)
    fun pluginLoad(){
        val mailer = mailerBuilder("smtp.qq.com",587,"no-replay-revoist@qq.com","nfxiwxpavjtvdjdh")
        MailerManager.defaultMailer = mailer
        val ref = Reflections("cn.revoist.lifephoton")
        //自动使用插件
        for (clazz in ref.getTypesAnnotatedWith(AutoUse::class.java)) {
            clazz.getAnnotation(AutoUse::class.java)?.let {
                if (it.version.num <= SYSTEM_VERSION.num) {
                    val plugin = clazz.getField("INSTANCE").get(null)
                    if (plugin is Plugin) usePlugin(plugin)
                }
            }
        }
        //自动注册路由
        for (clazz in ref.getTypesAnnotatedWith(AutoRegister::class.java)) {
            clazz.getAnnotation(AutoRegister::class.java)?.let {
                if (it.version.num <= SYSTEM_VERSION.num) {
                    val obj = clazz.getField("INSTANCE").get(null)
                    val plugin = getPlugin(it.pluginId)
                    if (obj is RoutePage) plugin?.registerPage(obj)
                }
            }
        }
        //自动注册路由组
        for (clazz in ref.getTypesAnnotatedWith(Gateway::class.java)) {
            clazz.getAnnotation(Gateway::class.java)?.let { gateway->
                val instance = clazz.getField("INSTANCE").get(null)
                val plugin = getPlugin(gateway.pluginId)
                clazz.declaredMethods.filter {
                    it.kotlinFunction?.annotations?.filterIsInstance<Route>()?.first() != null
                }.forEach {
                    val route = it.kotlinFunction!!.annotations.filterIsInstance<Route>().first()
                    val path = if (route.path == "&empty"){
                        formatConversion(it.name)
                    }else{
                        route.path
                    }
                    plugin?.registerRoute(HttpMethod.parse(route.method.uppercase()),gateway.root + "/" + path ,route.auth,route.inject){
                        println(it.parameters.joinToString(","))
                        it.kotlinFunction!!.call(instance,call, Continuation<Unit>(call.coroutineContext) {})
                    }
                }
            }
        }
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
    enum class SystemVersion(val num:Int,val views:List<String>){
        NORMAL(0, arrayListOf(
            "genome",
        )),
        ADVANCED(1, arrayListOf(
            "auth","genome"
        )),
        PROFESSIONAL(2, arrayListOf(
            "analysis","auth","genome","enrichment"
        )),
        AI(3, arrayListOf("analysis","auth","genome","enrichment"))
    }
}