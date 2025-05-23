package cn.revoist.lifephoton.module.funga.services

import cn.revoist.lifephoton.module.authentication.analysisTemplate
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.module.authentication.sendEmail
import cn.revoist.lifephoton.module.authentication.sendEmailNotice
import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.ai.chat.AnalysisAssistant
import cn.revoist.lifephoton.module.funga.ai.chat.ChatAssistant
import cn.revoist.lifephoton.module.funga.data.core.MilvusDatabase.search
import cn.revoist.lifephoton.module.funga.data.entity.inneral.AnalysisResult
import cn.revoist.lifephoton.module.funga.data.entity.inneral.PredictGene
import cn.revoist.lifephoton.module.funga.data.entity.inneral.SearchContainer
import cn.revoist.lifephoton.module.funga.data.entity.mapper.GeneInteractionMapper
import cn.revoist.lifephoton.module.funga.data.entity.request.GeneListWithDatabaseRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationFunGenesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationPredictGenesRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.ImputationResultRequest
import cn.revoist.lifephoton.module.funga.data.entity.response.FuncGeneResponse
import cn.revoist.lifephoton.module.funga.data.entity.response.PredictGeneResponse
import cn.revoist.lifephoton.module.funga.data.table.GeneInteractionTable
import cn.revoist.lifephoton.module.funga.data.table.GenePhenotypeTable
import cn.revoist.lifephoton.module.funga.tools.asFungaId
import cn.revoist.lifephoton.module.funga.tools.asSymbol
import cn.revoist.lifephoton.plugin.data.bind
import cn.revoist.lifephoton.plugin.data.mapsWithColumn
import cn.revoist.lifephoton.plugin.data.processor.MergeData
import cn.revoist.lifephoton.plugin.data.processor.join
import cn.revoist.lifephoton.plugin.data.sqltype.gson
import cn.revoist.lifephoton.tools.submit
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.runBlocking
import org.ktorm.dsl.*
import java.io.FileReader
import java.util.Date
import kotlin.random.Random

/**
 * @author 6hisea
 * @date  2025/4/19 16:19
 * @description: None
 */
object AnalysisService {
    val fileManager = FileManagementAPI.createStaticFileManager(FungaPlugin)
    val tempContainer = FungaPlugin.dataManager.useTempContainer<AnalysisResult>()
    fun nohupImputationFunGenes(request: ImputationFunGenesRequest,user:UserDataEntity?): String {
        val id = (System.currentTimeMillis() + Random(1000).nextInt()).toString()
        submit(-1,-1){
            try {
                val startDate = Date().toString()
                val funcGenes = imputationFunGenes(request)
                val outerGenes = imputationOuterGene(request)
                val predictGenes = arrayListOf<MergeData<java. util. HashMap<String, Any>>>()
                funcGenes.forEach {fd->
                    val preReq = ImputationPredictGenesRequest()
                    preReq.geneList = request.genes
                    preReq.setDatabases(arrayListOf(fd.database))
                    preReq.genes = fd.data.map { it.gene }
                    preReq.degree = request.degree
                    predictGenes.add(imputationPredictGene(preReq).find { it.database == fd.database }!!)
                }
                val req = GeneListWithDatabaseRequest()
                req.setDatabases(request.dbs())
                val g1 = funcGenes.map { it.data.map { it.gene } }.flatten()
                val g2 = outerGenes.map { it.data.map { it.gene } }.flatten()
                val g3 = predictGenes.filter { it.data["genes"] != null }.map { (it.data["genes"] as HashMap<String,Int>).keys }.flatten()
                req.genes = g1 + g2 +g3

                val data = hashMapOf(
                    "input" to hashMapOf(
                        "genes" to request.genes,
                        "phenotypes" to request.phenotypes,
                        "type" to request.type
                    ),
                    "func" to funcGenes,
                    "outer" to outerGenes,
                    "predict" to predictGenes,
                    "graph" to GeneService.getInteractionsByGeneList(req),
                )
                data["discussion"] = try {
                    ChatAssistant.INSTANCE.generateDiscretion(gson.toJson(data))
                }catch (e:Exception){
                    "无"
                }
                fileManager.putStaticFileWithTemp(id){
                    it.writeText(
                        gson.toJson(data)
                    )
                }
                val endDate = Date().toString()
                if (user != null){
                    runBlocking {
                        user.sendEmailNotice("【FUNGA-Analysis】您的分析已经完成",analysisTemplate(
                            "【FUNGA-Analysis】您的分析已经完成",
                            user.username,
                            id,
                            startDate,
                            endDate,
                            "http://localhost:5173/genePhenotypeDetail?id=${id}"
                        ))
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                runBlocking {
                    user?.sendEmail("【FUNGA-Analysis】您的分析已经失败","原因：${e.toString()}.")
                }
            }
        }
        return id
    }

    fun imputationFunGenes(request:ImputationFunGenesRequest): List<MergeData<ArrayList<FuncGeneResponse>>> {
        return join(request.dbs()){db->
            val ids = request.genes.asFungaId(db)
            val phenotypes = HashMap<String,List<String>>()

            val funcGenes = arrayListOf<FuncGeneResponse>()
            ids.forEach {
                phenotypes[it] = FungaPlugin.dataManager.useDatabase(db)
                    .mapsWithColumn(GenePhenotypeTable,GenePhenotypeTable.phenotype){
                        where {
                            GenePhenotypeTable.gene eq it
                        }
                    }.map {
                        it["phenotype"].toString()
                    }
            }
            phenotypes.forEach {gene,ps->
                var ppp = ps
                //检验一下是否相关
                val isRelated = if (request.type == "union"){
                    ppp = AnalysisAssistant.INSTANCE.isPhenotypeRelatedAny(request.phenotypes,ps).filter { it != "无" }
                    ppp.isNotEmpty()
                }else{
                    ppp = AnalysisAssistant.INSTANCE.isPhenotypeRelatedAll(request.phenotypes,ps)
                    ppp.filter { it != "无" }.isNotEmpty() && !ppp.contains("无")
                }
                if(isRelated){
                    funcGenes.add(FuncGeneResponse(gene.asSymbol(db),ppp))
                }
            }
            funcGenes
        }
    }
    fun imputationPredictGene(request: ImputationPredictGenesRequest,currentDegree:Int = 1): List<MergeData<HashMap<String, Any>>> {
        return join(request.dbs()){db->
            GeneService.getInteractionsByPGR(findInteractions(db,request,currentDegree),db)
        }
    }
    private fun findInteractions(db:String,request:ImputationPredictGenesRequest,currentDegree: Int): PredictGeneResponse {
        if (request.genes.isEmpty()) return PredictGeneResponse()
        val genes = request.genes.asFungaId(db)
        val geneList = request.geneList.asFungaId(db)
        val dg1 = PredictGeneResponse()
        dg1[currentDegree] = FungaPlugin.dataManager.useDatabase(db)
            .bind(GeneInteractionTable,GeneInteractionMapper::class.java){
                select(GeneInteractionTable.gene1,GeneInteractionTable.gene2)
                    .where {
                        ((GeneInteractionTable.gene1 inList geneList) and (GeneInteractionTable.gene2 inList geneList)) and ((GeneInteractionTable.gene1 inList genes) or (GeneInteractionTable.gene2 inList genes))
                    }
            }.map {
                if (it.gene1 in request.genes){
                    it.gene2
                }else{
                    it.gene1
                }
            }
        return if (currentDegree <= request.degree){
            request.genes = dg1[currentDegree]!!
            findInteractions(db,request,currentDegree+1) + dg1
        }else{
            dg1
        }
    }
    fun imputationOuterGene(request:ImputationFunGenesRequest): List<MergeData<List<FuncGeneResponse>>> {
        return join(request.dbs()){db->
            val result = HashMap<String,ArrayList<String>>()
            val outerGenes = ArrayList<FuncGeneResponse>()
            val ids = request.genes.asFungaId(db)
            request.phenotypes.forEach {
                GenePhenotypeTable.search(db, hashMapOf("phenotype" to it),request.topK).forEach {
                    if (!ids.contains(it["gene"])){
                        if (!result.containsKey(it["gene"])){
                            result[it["gene"].toString()] = arrayListOf()
                        }
                        result[it["gene"]]?.add(it["phenotype"].toString())
                    }
                }
            }
            result.forEach{gene,ps->
                /*
                var ppp = ps as List<String>
                //检验一下是否相关
                val isRelated = if (request.type == "union"){
                    ppp = AnalysisAssistant.INSTANCE.isPhenotypeRelatedAny(request.phenotypes,ps).filter { it != "无" }
                    ppp.isNotEmpty()
                }else{
                    ppp = AnalysisAssistant.INSTANCE.isPhenotypeRelatedAll(request.phenotypes,ps)
                    ppp.filter { it != "无" }.isNotEmpty() && !ppp.contains("无")
                }
                if(isRelated){
                    outerGenes.add(FuncGeneResponse(gene.asSymbol(db),ppp))
                }
                 */
                outerGenes.add(FuncGeneResponse(gene.asSymbol(db),ps))
            }
            return@join outerGenes.filter { it.phenotypes.isNotEmpty() }
        }
    }
    fun isReadyImputation(id:String):Boolean{
        val f = fileManager.getStaticFile(id)
        return f.exists()
    }
    fun getImputation(request:ImputationResultRequest):Any{
        val dbs = request.dbs()
        val result = tempContainer.callMemory(request.id,gson.fromJson(JsonReader(FileReader(fileManager.getStaticFile(request.id))), AnalysisResult::class.java))
        return when(request.type){
            "discussion" -> result.discussion
            "graph" -> {
                result.graph.map { it.data!! }.flatten().filter {
                    it.gene1 in request.genes && it.gene2 in request.genes
                }
            }
            "input"-> {
                result.input
            }
            "func" -> {
                result.func.filter { dbs.contains(it.database) }
            }
            "outer" -> {
                result.outer.filter { dbs.contains(it.database) }
            }
            "predict" -> {
                val r = arrayListOf<SearchContainer<List<PredictGene>>>()
                result.predict.filter { dbs.contains(it.database) }.forEach {
                    val nsc = SearchContainer<List<PredictGene>>()
                    nsc.database = it.database
                    nsc.data = arrayListOf()
                    val pg = PredictGene()
                    val m = it.data!!.genes.filter { it.value <= request.degree }
                    val mm = HashMap<String,Int>()
                    mm.putAll(m)
                    pg.genes = mm
                    pg.interactions = it.data!!.interactions.filter { it.gene1 in mm.keys || it.gene2 in mm.keys }
                    (nsc.data as ArrayList<PredictGene>).add(pg)
                    r.add(nsc)
                }
                r
            }
            else->{
                result.func.filter { dbs.contains(it.database) }
            }
        }
    }
}