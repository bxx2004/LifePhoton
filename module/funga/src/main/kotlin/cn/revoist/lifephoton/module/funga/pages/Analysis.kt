package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.authentication.asEntity
import cn.revoist.lifephoton.module.authentication.getUser
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.request.FuncGeneSortedRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationFunGenesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationResultRequest
import cn.revoist.lifephoton.module.funga.data.table.GenePhenotypeAnalysisTable
import cn.revoist.lifephoton.module.funga.data.table.GenePhenotypeTable
import cn.revoist.lifephoton.module.funga.services.AnalysisService
import cn.revoist.lifephoton.module.funga.tools.asFungaId
import cn.revoist.lifephoton.module.funga.tools.asSymbol
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.match
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.tools.checkNotNull
import io.ktor.server.routing.*
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where


/**
 * @author 6hisea
 * @date  2025/4/19 16:18
 * @description: None
 */
@RouteContainer("funga","analysis")
object Analysis {
    @Route(GET)
    suspend fun getAnalysisResult(call: RoutingCall){
        call.match { isLogin() }
            .then {
                val user = call.getUser().asEntity!!
                call.ok(
                    FungaPlugin.dataManager.useDatabase()
                        .maps(GenePhenotypeAnalysisTable, GenePhenotypeAnalysisTable.result, GenePhenotypeAnalysisTable.id){
                            where {
                                GenePhenotypeAnalysisTable.user_id eq user.id
                            }
                        }
                )
            }.default {
                error("You not login.")
            }
    }
    @Route
    suspend fun nohupImputation(call: RoutingCall){
        val request = call.requestBody(ImputationFunGenesRequest::class.java)
        val isLogin = call.isLogin()
        val user = if (isLogin) {
            call.getUser().asEntity
        }else{
            null
        }
        if (user == null){
            call.error("You not login.")
            return
        }
        call.ok(AnalysisService.nohupImputationFunGenes(request,user))
    }


    @Route
    suspend fun getImputation(call: RoutingCall){
        val request = call.requestBody(ImputationResultRequest::class.java)
        call.checkNotNull(request.id)
        if (AnalysisService.isReadyImputation(request.id)) {
            val user = if (call.isLogin()) {
                call.getUser().asEntity
            }else{
                null
            }
            if (user == null){
                call.error("You not login.")
                return
            }
            val res = AnalysisService.getImputation(request,user)
            if (res is String && !res.contains("\n")){
                call.error(res)
            }else{
                call.ok(res)
            }

        }else{
            call.error("Not query id.")
        }
    }
    @Route(GET)
    suspend fun isReadyImputation(call: RoutingCall){
        val id = call.queryParameters["id"]
        call.checkNotNull(id)
        call.ok(
            AnalysisService.isReadyImputation(id!!)
        )
    }
    @Route(POST)
    suspend fun sortedGene(call: RoutingCall){
        val request = call.requestBody(FuncGeneSortedRequest::class.java)
        val db = request.dbs()[0]
        val genes = hashMapOf<String, List<String>>()
        request.genes.asFungaId(db).forEach {
            genes[it.asSymbol(db)] =
                FungaPlugin.dataManager.useDatabase(db).from(
                    GenePhenotypeTable
                ).select(GenePhenotypeTable.phenotype).where {
                    GenePhenotypeTable.gene eq it
                }.map {
                    it.get(GenePhenotypeTable.phenotype).toString()
                }
        }
        call.ok(AnalysisService.funcGeneSort(
            genes,request.phenotype,db
        ))
    }
}