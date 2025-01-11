package cn.revoist.lifephoton.extensions.filem

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse
import java.io.File

/**
 * @author 6hisea
 * @date  2025/1/10 11:08
 * @description: None
 */
@AutoUse(Booster.SystemVersion.ADVANCED)
object FileManagement : Plugin(){
    override val name: String
        get() = "FileManagement"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "beta-1"
    override val id: String
        get() = "file-management"


    override fun configure() {
        optional("path","/data/LifePhoton/file-management")
        optional("count-limit",100)
        optional("size-limit",500 * 1024)
        optional("no-login-size-limit",10 * 1024)
    }
    override fun load() {
        val dir = File(option<String>("path"))
        if (!dir.exists()){
            dir.mkdirs()
        }
    }
}