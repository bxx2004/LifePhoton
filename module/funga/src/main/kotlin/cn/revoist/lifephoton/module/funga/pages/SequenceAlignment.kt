package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.authentication.data.Tools
import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.funga.FungaOption
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneListWithDatabaseRequest
import cn.revoist.lifephoton.module.funga.services.ToolsService
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.POST
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.checkParameters
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.server.routing.RoutingCall

/**
 * @author 6hisea
 * @date  2025/5/28 20:12
 * @description: None
 */

@RouteContainer("funga","tools")
object SequenceAlignment {
    @Route(GET)
    suspend fun alignment(call: RoutingCall){
        call.checkParameters("file","type","species","eValue")
        val fileId = call.queryParameters["file"]!!
        val type = call.parameters["type"]!!
        val species = call.parameters["species"]!!
        val eValue = call.parameters["eValue"]!!.toDouble()
        call.ok(ToolsService.alignment(FileManagementAPI.findFileByIdentifier(fileId)!!,species,type,eValue))
    }
    @Route(GET)
    suspend fun getAllAlignmentSpecies(call: RoutingCall){
        call.ok(FungaOption.databases)
    }
    @Route(GET)
    suspend fun isReadyAlignment(call: RoutingCall){
        call.ok(ToolsService.isReady(call.queryParameters["id"]!!))
    }
    @Route(GET)
    suspend fun getAlignment(call: RoutingCall){
        call.ok(ToolsService.getAlignment(call.queryParameters["id"]!!))
    }
    @Route(POST)
    suspend fun idMapping(call: RoutingCall){
        val body = call.requestBody(GeneListWithDatabaseRequest::class.java)
        call.ok(ToolsService.idMapping(body))
    }
}