package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneListWithDatabaseRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneWithDatabasesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.SentenceRequest
import cn.revoist.lifephoton.module.funga.data.table.GeneTable
import cn.revoist.lifephoton.module.funga.services.GeneService
import cn.revoist.lifephoton.module.funga.tools.asFungaId
import cn.revoist.lifephoton.plugin.data.json.jsonObject
import cn.revoist.lifephoton.plugin.data.mapsWithColumn
import cn.revoist.lifephoton.plugin.data.processor.join
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.tools.checkNotNull
import io.ktor.server.routing.*
import org.ktorm.dsl.inList
import org.ktorm.dsl.where

/**
 * @author 6hisea
 * @date  2025/4/13 11:28
 * @description: None
 */
@RouteContainer("funga","gene")
object Gene {
    @Route(POST)
    @Api(
        "根据提交的基因名称查询在Funga平台的Id",
        [
            Param("gene","基因名称"),
            Param("databases","数据库")
        ]
    )
    suspend fun getFungaId(call: RoutingCall){
        val request = call.requestBody(GeneWithDatabasesRequest::class.java)
        call.ok(
            join(request.dbs()){
                it.asFungaId(it)
            }
        )

    }
    @Route(POST)
    @Api(
        "根据提供的基因ID，查询并返回该基因的基本信息",
        [
            Param("id","基因ID"),
            Param("databases","数据库")
        ]
    )
    suspend fun getInformationById(call: RoutingCall){
        val request = call.requestBody(GeneWithDatabasesRequest::class.java)
        call.checkNotNull(request.gene)
        call.ok(
            GeneService.getInformationById(request.gene,request.dbs())
        )
    }
    @Route(POST)
    suspend fun mappingSymbol(call: RoutingCall){
        val request = call.requestBody(GeneListWithDatabaseRequest::class.java)
        call.ok(
            jsonObject {
                for (i in FungaPlugin.dataManager.useDatabase(request.dbs()[0])
                    .mapsWithColumn(GeneTable,GeneTable.symbol,GeneTable.otherId,GeneTable.fungaId){
                        where {
                            GeneTable.fungaId inList request.genes
                        }
                    }.map {
                        if (it["symbol"] == null){
                            val oi = it["other_id"] as List<String>
                            if (oi.isEmpty()){
                                Pair(it["funga_id"].toString(),it["funga_id"].toString())
                            }else{
                                Pair(it["funga_id"].toString(),oi[0])
                            }
                        }else{
                            Pair(it["funga_id"].toString(),it["symbol"]!!)
                        }
                    }){
                    put(i.first,i.second)
                }
            }
        )
    }
    @Route(POST)
    suspend fun searchGeneBySentence(call: RoutingCall){
        val request = call.requestBody(SentenceRequest::class.java)
        call.checkNotNull(request.sentence)
        call.ok(
            GeneService.searchGeneBySentence(request)
        )
    }
    @Route(POST)
    suspend fun getInteractionsByGeneList(call: RoutingCall){
        val request = call.requestBody(GeneListWithDatabaseRequest::class.java)
        call.checkNotNull(request.genes)
        call.ok(
            GeneService.getInteractionsByGeneList(request)
        )
    }
    @Route(POST)
    suspend fun getInteractionsById(call: RoutingCall){
        val request = call.requestBody(GeneWithDatabasesRequest::class.java)
        call.checkNotNull(request.gene)
        call.ok(
            GeneService.getInteractionsById(request)
        )
    }
    @Route(POST)
    suspend fun getPhenotypesById(call: RoutingCall){
        val request = call.requestBody(GeneWithDatabasesRequest::class.java)
        call.ok(
            GeneService.getPhenotypesById(request.gene,request.dbs())
        )
    }
}