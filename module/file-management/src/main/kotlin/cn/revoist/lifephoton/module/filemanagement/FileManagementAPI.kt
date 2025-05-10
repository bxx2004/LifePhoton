package cn.revoist.lifephoton.module.filemanagement

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
    fun createStaticFileManager(plugin: Plugin):StaticFileManager{
        return StaticFileManager(plugin.id)
    }

    class StaticFileManager(private val uniqueId:String){
        fun putStaticFileWithTemp(p:String,func:(file:File)->Unit){
            val file = File(FileManagement.option<String>("path") + "/static/$uniqueId/$p.tmp")
            if (!file.parentFile.exists()){
                file.parentFile.mkdirs()
            }
            if (!file.exists()){
                file.createNewFile()
            }
            func(file)
            file.renameTo(File(file.absolutePath.replace(".tmp","")))
        }
        fun putStaticFile(p:String,func:(file:File)->Unit){
            val file = File(FileManagement.option<String>("path") + "/static/$uniqueId/$p")
            if (!file.parentFile.exists()){
                file.parentFile.mkdirs()
            }
            if (!file.exists()){
                file.createNewFile()
            }
            func(file)
        }
        fun getStaticFile(p:String):File{
            return File(FileManagement.option<String>("path") + "/static/$uniqueId/$p")
        }
    }
}