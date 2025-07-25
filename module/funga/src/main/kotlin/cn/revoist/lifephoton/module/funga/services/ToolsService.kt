package cn.revoist.lifephoton.module.funga.services

import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneListWithDatabaseRequest
import cn.revoist.lifephoton.module.funga.data.table.GeneTable
import cn.revoist.lifephoton.module.funga.tools.asFungaId
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.data.mapsWithColumn
import cn.revoist.lifephoton.plugin.data.processor.MergeData
import cn.revoist.lifephoton.plugin.data.processor.join
import cn.revoist.lifephoton.tools.submit
import org.ktorm.dsl.inList
import org.ktorm.dsl.where
import java.io.File
import kotlin.random.Random

/**
 * @author 6hisea
 * @date  2025/5/28 20:30
 * @description: None
 */
object ToolsService {
    val fileManager = FileManagementAPI.createStaticFileManager(FungaPlugin,"alignment")
    fun alignment(file: File,db:String,type: String,eValue: Double = 0.001):String{

        fun getCommand(target: File):String{
            val symbol = if (type == "dna"){
                "blastx"
            }else{
                "blastp"
            }
            return if (FungaPlugin.getOS() == Plugin.OS.WINDOWS){
                "\"${FungaPlugin.diamondExec}\" ${symbol} --db \"${FungaPlugin.dmnd(db)}\" --query \"${file.absolutePath}\" --out \"${target.absolutePath}\"  --max-target-seqs 1 --evalue $eValue --threads 64"
            }else{
                "${FungaPlugin.diamondExec} ${symbol} --db ${FungaPlugin.dmnd(db)} --query ${file.absolutePath} --out ${target.absolutePath}  --max-target-seqs 1 --evalue $eValue --threads 64"
            }
        }

        val id = (System.currentTimeMillis() + Random(1000).nextInt()).toString()
        submit(-1,-1){
            try {
                fileManager.putStaticFileWithTemp(id){
                    val command = getCommand(it)
                    val process = Runtime.getRuntime().exec(command)
                    process.waitFor()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return id
    }
    fun isReady(id:String):Boolean{
        return fileManager.getStaticFile(id).exists()
    }
    fun getAlignment(id:String): List<HashMap<String,Any>>{
        val result = arrayListOf<HashMap<String,Any>>()
        val file = fileManager.getStaticFile(id)
        file.readLines().forEach {
            val line = it.split("\t")
            result.add(hashMapOf(
                "qseqid" to line[0],
                "sseqid" to line[1],
                "pident" to line[2].toDouble(),
                "length" to line[3].toInt(),
                "mismatch" to line[4].toInt(),
                "gapopen" to line[5].toInt(),
                "qstart" to line[6].toInt(),
                "qend" to line[7].toInt(),
                "sstart" to line[8].toInt(),
                "send" to line[9].toInt(),
                "evalue" to line[10].toDouble(),
                "bitscore" to line[11].toDouble(),
            ))
        }
        return result
    }
    fun idMapping(req: GeneListWithDatabaseRequest):List<MergeData<List<HashMap<String, Any?>>>>{
         return join(req.dbs()){db->
             val genes = req.genes.asFungaId(db)
             return@join FungaPlugin.dataManager.useDatabase(db)
                .mapsWithColumn(GeneTable, GeneTable.fungaId, GeneTable.symbol, GeneTable.otherId){
                    where{
                        GeneTable.fungaId inList genes
                    }
                }
        }
    }
}