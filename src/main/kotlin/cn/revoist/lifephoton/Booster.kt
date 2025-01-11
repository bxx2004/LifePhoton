package cn.revoist.lifephoton

import cn.revoist.lifephoton.ktors.getPlugin
import cn.revoist.lifephoton.ktors.usePlugin
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoRegister
import cn.revoist.lifephoton.plugin.anno.AutoUse
import cn.revoist.lifephoton.plugin.route.RoutePage
import org.ktorm.database.Database
import org.reflections.Reflections

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
        val ref = Reflections("cn.revoist.lifephoton")
        for (clazz in ref.getTypesAnnotatedWith(AutoUse::class.java)) {
            clazz.getAnnotation(AutoUse::class.java)?.let {
                if (it.version.num <= SYSTEM_VERSION.num) {
                    val plugin = clazz.getField("INSTANCE").get(null)
                    if (plugin is Plugin) usePlugin(plugin)
                }
            }
            /**
             * clazz.getAnnotation(Pages::class.java)?.let {
             *                 if (it.version.num <= SYSTEM_VERSION.num) {
             *                     val pack = it.pack
             *                     val refPages = Reflections(pack)
             *                     refPages.allTypes.forEach {
             *
             *                     }
             *                 }
             *             }
             */
        }
        for (clazz in ref.getTypesAnnotatedWith(AutoRegister::class.java)) {
            clazz.getAnnotation(AutoRegister::class.java)?.let {
                if (it.version.num <= SYSTEM_VERSION.num) {
                    val obj = clazz.getField("INSTANCE").get(null)
                    val plugin = getPlugin(it.pluginId)
                    if (obj is RoutePage) plugin?.registerPage(obj)
                }
            }
        }
    }

    enum class SystemVersion(val chinese:String,val num:Int){
        NORMAL("普通版",0),
        ADVANCED("进阶版",1),
        PROFESSIONAL("专业版",2),
        AI("AI版",3)
    }
}