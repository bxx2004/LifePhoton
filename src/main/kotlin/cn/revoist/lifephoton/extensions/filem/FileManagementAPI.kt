package cn.revoist.lifephoton.extensions.filem

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.PluginAPI
import java.io.File

/**
 * @author 6hisea
 * @date  2025/1/22 16:57
 * @description: None
 */
object FileManagementAPI : PluginAPI{
    override val plugin: Plugin
        get() = FileManagement
    fun findFileByIdentifier(identifier: String): File? {
        val dir = File(FileManagement.option<String>("path"))
        val target = dir.listFiles()!!.find { it.name.contains(identifier) }
        return target
    }
}