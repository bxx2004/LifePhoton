package cn.revoist.lifephoton.module.genome.pages

import cn.revoist.lifephoton.module.genome.Genome
import cn.revoist.lifephoton.module.genome.data.entity.request.GeneBasicRequest
import cn.revoist.lifephoton.module.genome.data.entity.request.GeneInfoRequest
import cn.revoist.lifephoton.module.genome.data.entity.request.GeneStructureRequest
import cn.revoist.lifephoton.module.genome.data.table.GenomeGeneStructure
import cn.revoist.lifephoton.module.genome.service.GeneBaseService
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import io.ktor.server.routing.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.where
import org.ktorm.schema.ColumnDeclaring
/**
 * @author 6hisea
 * @date  2025/1/25 10:32
 * @description: None
 */
@RouteContainer("genome","gene-base")
object GeneBase {
    @Route(POST)
    @Api(
        "根据提交的信息查询到基因的全部基本信息",
        [
            Param("type","查询类型"),
            Param("data","查询对应的数据id(gene_id);region(start,end);name(gene_name)")
        ]
    )
    suspend fun getGeneInformation(call:RoutingCall){
        val request = call.requestBody(GeneInfoRequest::class.java)
        if (arrayOf("id","name","region").contains(request.type)) {
            call.ok(GeneBaseService.searchGeneInfoByGeneInfoRequest(request))
        }else{
            call.error("Please input truth type.")
        }
    }
    @Route(POST)
    @Api(
        "根据基因ID获取基因序列信息",
        [
            Param("geneId","基因ID"),
            Param("^speciesName","物种名称"),
            Param("^assembledVersion","物种版本")
        ]
    )
    suspend fun getGeneSequenceByGeneId(call: RoutingCall){
        val request = call.requestBody(GeneBasicRequest::class.java)
        call.ok(
            Genome.dataManager.useDefaultDatabase()
                .maps(cn.revoist.lifephoton.module.genome.data.table.GenomeGeneSequence, cn.revoist.lifephoton.module.genome.data.table.GenomeGeneSequence.id){
                    where {
                        val conditions = ArrayList<ColumnDeclaring<Boolean>>()
                        conditions += cn.revoist.lifephoton.module.genome.data.table.GenomeGeneSequence.geneId eq request.geneId
                        if (request.speciesName != null){
                            conditions += cn.revoist.lifephoton.module.genome.data.table.GenomeGeneSequence.speciesName eq request.speciesName!!
                        }
                        if (request.assembledVersion != null){
                            conditions += cn.revoist.lifephoton.module.genome.data.table.GenomeGeneSequence.assembledVersion eq request.assembledVersion!!
                        }
                        conditions.reduce{ a, b -> a and b }
                    }
                }
        )
    }
    @Route(POST)
    @Api(
        "根据基因ID获取基因结构信息",
        [
            Param("geneId","基因ID"),
            Param("^speciesName", "物种名称"),
            Param("^assembledVersion", "物种版本"),
            Param("offset","开启偏移")
        ]
    )
    suspend fun getGeneStructureByGeneId(call: RoutingCall){
        val request = call.requestBody(GeneStructureRequest::class.java)
        val result = Genome.dataManager.useDefaultDatabase()
            .maps(GenomeGeneStructure, GenomeGeneStructure.id){
                where {
                    val conditions = ArrayList<ColumnDeclaring<Boolean>>()
                    conditions += GenomeGeneStructure.geneId eq request.geneId
                    if (request.speciesName != null){
                        conditions += GenomeGeneStructure.speciesName eq request.speciesName!!
                    }
                    if (request.assembledVersion != null){
                        conditions += GenomeGeneStructure.assembledVersion eq request.assembledVersion!!
                    }
                    conditions.reduce { a, b -> a and b }
                }
            }
        if (request.offset){
            val offset = result.sortedBy { it["start"] as Long }[0]["start"] as Long
            result.forEach {
                it["start"] = it["start"] as Long - offset
                it["end"] = it["end"] as Long - offset
            }
        }
        call.ok(result)
    }
    @Route(POST)
    @Api(
        "根据GeneId获取注释信息",
        [
            Param("geneId","基因ID"),
            Param("^speciesName","物种名称"),
            Param("^assembledVersion","物种版本")
        ]
    )
    suspend fun getGeneAnnotationByGeneId(call: RoutingCall){
        val request = call.requestBody(cn.revoist.lifephoton.module.genome.data.entity.request.GeneBasicRequest::class.java)
        call.ok(GeneBaseService.getGeneAnnotationByGeneId(request))
    }
}