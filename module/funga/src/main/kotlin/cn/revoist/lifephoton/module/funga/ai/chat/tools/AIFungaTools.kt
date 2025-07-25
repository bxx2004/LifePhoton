package cn.revoist.lifephoton.module.funga.ai.chat.tools

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.ai.assistant.ChatAssistant
import cn.revoist.lifephoton.module.funga.data.core.MilvusDatabase.search
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneListWithDatabaseRequest
import cn.revoist.lifephoton.module.funga.data.table.DBInfoTable
import cn.revoist.lifephoton.module.funga.data.table.GeneInteractionTable
import cn.revoist.lifephoton.module.funga.data.table.GenePhenotypeTable
import cn.revoist.lifephoton.module.funga.data.table.GeneTable
import cn.revoist.lifephoton.module.funga.services.GeneService
import cn.revoist.lifephoton.module.funga.tools.asFungaId
import cn.revoist.lifephoton.module.funga.tools.tryMappings
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.data.sqltype.gson
import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import dev.langchain4j.agent.tool.ToolMemoryId
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.dsl.where

/**
 * @author 6hisea
 * @date  2025/5/26 20:04
 * @description: None
 */
object AIFungaTools {

    @Tool("通过模糊信息(如表型、描述)搜索基因")
    fun getGeneInformationByDescription(@ToolMemoryId id:Int,@P("基因的描述") description: String,@P("物种名称") speciesName: String):String{
        val dbName = ChatAssistant.INSTANCE.findDatabase(speciesName, FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() })
        /*
                val a = GeneTable.search(
            dbName,
            hashMapOf("description" to description),
            50
        ).filter { (it["score"] as Number).toDouble() >= 0.6 }.joinToString("\n") { "物种名称是：${dbName}，基因名称是:${it["funga_id"]},基因描述是:${it["description"]}" }
         */
        val b = GenePhenotypeTable.search(
            dbName,
            hashMapOf("phenotype" to description),
            50
        ).tryMappings(dbName).joinToString("\n") { "物种名称是：${dbName}，基因名称是:${it["gene"]},表型描述是:${it["phenotype"]}" }
        return "$b\n当然这只是一部分"
    }



    @Tool("使用基因标识查询指定基因的所有信息")
    fun getGeneInformation(@ToolMemoryId id:Int,@P("用来查询的基因标识") geneId: String,@P("物种名称") speciesName: String): String {
        val dbName = ChatAssistant.INSTANCE.findDatabase(speciesName, FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() })
        return try {
            gson.toJson(FungaPlugin.dataManager.useDatabase(dbName.lowercase()).maps(GeneTable){
                where {
                    GeneTable.fungaId eq geneId.asFungaId(dbName.lowercase())
                }
            })
        }catch (e: Exception){
            "没有找到该基因的详细信息"
        }
    }
    @Tool("查询多个基因间是否互作")
    fun getGeneInteractionByTgene(@ToolMemoryId id:Int, @P("基于列表") geneList: List<String>, @P("物种名称") speciesName: String): String{
        val dbName = ChatAssistant.INSTANCE.findDatabase(speciesName, FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() })
        val req = GeneListWithDatabaseRequest()
        req.genes = geneList
        req.setDatabases(arrayListOf(dbName))

        return try {
            GeneService.getInteractionsByGeneList(req).joinToString("\n") {
                "物种为:${it.database}的互作有：" + it.data.map {
                    "${it["gene1"]}-${it["gene2"]}通过${it["type"]}互作，参考文献为:${it["references"]}"
                }
            }
        }catch (e: Exception){
            "没有找到该基因的详细信息"
        }
    }
    @Tool("使用基因标识查询基因的互作信息")
    fun getGeneInteraction(@ToolMemoryId id:Int,@P("用来查询的基因标识") geneId: String,@P("物种名称") speciesName: String): String{
        val dbName = ChatAssistant.INSTANCE.findDatabase(speciesName, FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() })
        return try {
            FungaPlugin.dataManager.useDatabase(dbName).maps(GeneInteractionTable) {
                where {
                    (GeneInteractionTable.gene1 eq geneId.asFungaId(dbName)) or (GeneInteractionTable.gene2 eq geneId.asFungaId(
                        dbName
                    ))
                }
            }.joinToString("\n") {
                "${it["gene1"]}-${it["gene2"]}通过${it["type"]}互作，参考文献为:${it["references"]}"
            }
        }catch (e: Exception){
            "没有找到该基因的详细信息"
        }
    }
    @Tool("使用基因标识查询基因的表型关联信息")
    fun getGenePhenotype(@ToolMemoryId id:Int,@P("用来查询的基因标识") geneId: String,@P("物种名称") speciesName: String): String{
        val dbName = ChatAssistant.INSTANCE.findDatabase(speciesName, FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() })
        return try {
            gson.toJson(FungaPlugin.dataManager.useDatabase(dbName).maps(GenePhenotypeTable){
                where {
                    GenePhenotypeTable.gene eq geneId.asFungaId(dbName)
                }
            })
        }catch (e: Exception){
            "没有找到该基因的详细信息"
        }
    }
    @Tool("获取当前所有物种的数据库")
    fun getDatabase(@ToolMemoryId id:Int):List<String>{
        return FungaPlugin.dataManager.useDatabase().maps(DBInfoTable,DBInfoTable.id).map { it["name"].toString() }
    }
    @Tool("获取分析基因和表型直接关联的建议")
    fun analysis(@ToolMemoryId id:Int): String{
        return "无建议"
    }
}