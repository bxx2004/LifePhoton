package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.authentication.asEntity
import cn.revoist.lifephoton.module.authentication.getUser
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationFunGenesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationPredictGenesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationResultRequest
import cn.revoist.lifephoton.module.funga.services.AnalysisService
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.tools.checkNotNull
import com.google.gson.stream.JsonReader
import io.ktor.server.routing.*
import java.io.FileReader


/**
 * @author 6hisea
 * @date  2025/4/19 16:18
 * @description: None
 */
@RouteContainer("funga","analysis")
object Analysis {

    @Route
    suspend fun imputationGenes(call:RoutingCall){
        val request = call.requestBody(ImputationFunGenesRequest::class.java)
        call.ok(AnalysisService.imputationFunGenes(request))
    }
    @Route
    suspend fun imputationPredictGenes(call:RoutingCall){
        val request = call.requestBody(ImputationPredictGenesRequest::class.java)
        call.ok(AnalysisService.imputationPredictGene(request))
    }
    @Route
    suspend fun imputationOuterGenes(call:RoutingCall){
        val request = call.requestBody(ImputationFunGenesRequest::class.java)
        call.ok(AnalysisService.imputationOuterGene(request))
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
        call.ok(AnalysisService.nohupImputationFunGenes(request,user))
    }


    @Route
    suspend fun getImputation(call: RoutingCall){
        val request = call.requestBody(ImputationResultRequest::class.java)
        call.checkNotNull(request.id)
        if (AnalysisService.isReadyImputation(request.id)) {
            call.ok(AnalysisService.getImputation(request))
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
}