package cn.revoist.lifephoton.module.mti.pages

import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.mti.MatingTypeImputation
import cn.revoist.lifephoton.module.mti.data.entity.request.ImputationRequest
import cn.revoist.lifephoton.plugin.data.sqltype.gson
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.tools.checkNotNull
import io.ktor.server.routing.*
import kotlinx.serialization.json.internal.decodeStringToJsonTree
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * @author 6hisea
 * @date  2025/3/2 20:21
 * @description: None
 */
@RouteContainer("mating-type-imputation","imputation")
object Imputation {
    @Route
    @Api("获取推断结果",
        [
        Param("matingResultMatrixFile","the matrix of mating result"),
        Param("osResultFile","the table result for OS experiment")
        ]
    )
    suspend fun getImputationResult(call: RoutingCall){
        val request = call.requestBody(ImputationRequest::class.java)
        val osResultFile = FileManagementAPI.findFileByIdentifier(request.osResultFile)
        val matingResultMatrixFile = FileManagementAPI.findFileByIdentifier(request.matingResultMatrixFile)
        call.checkNotNull(osResultFile,matingResultMatrixFile)
        val process = Runtime.getRuntime().exec(
            "python ${MatingTypeImputation.option<String>("exec")} ${matingResultMatrixFile!!.absolutePath} ${osResultFile!!.absolutePath}"
        )
        process.waitFor()
        val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        val outFile = File(matingResultMatrixFile!!.absolutePath + ".output.json")
        if (outFile.exists() && outFile.length() > 0) {
            call.ok(gson.fromJson(outFile.readText(),Any::class.java))
        }else{
            call.error("结果未生成: ${bufferedReader.readText()}")
        }
    }
}