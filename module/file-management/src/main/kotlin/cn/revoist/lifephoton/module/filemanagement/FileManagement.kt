package cn.revoist.lifephoton.module.filemanagement

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse
import java.io.File

/**
 * @author 6hisea
 * @date  2025/1/10 11:08
 * @description: None
 */
@AutoUse
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
        optional("path","/data/LifePhoton/file-management/")
        optional("size-limit",524288000)
        val path = File("/data/LifePhoton/file-management/")
        if (!path.exists()){
            path.mkdirs()
        }
        val patha = File("/data/LifePhoton/file-management/static/")
        if (!patha.exists()){
            patha.mkdirs()
        }
    }
    override fun load() {
        val dir = File(option<String>("path"))
        if (!dir.exists()){
            dir.mkdirs()
        }
    }
}