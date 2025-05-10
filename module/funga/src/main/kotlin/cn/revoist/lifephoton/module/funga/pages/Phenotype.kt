package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.entity.request.OntologyRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.PhenotypeRequest
import cn.revoist.lifephoton.module.funga.data.entity.response.POGraph
import cn.revoist.lifephoton.module.funga.data.table.PhenotypeOntologyQualifierTable
import cn.revoist.lifephoton.module.funga.data.table.PhenotypeOntologyTable
import cn.revoist.lifephoton.module.funga.services.PhenotypeService
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.ok
import cn.revoist.lifephoton.tools.checkNotNull
import io.ktor.server.routing.*
import org.ktorm.dsl.*

/**
 * @author 6hisea
 * @date  2025/4/19 11:41
 * @description: None
 */
@RouteContainer("funga","phenotype")
object Phenotype {
    @Route
    suspend fun findPhenotypeOntologyByPhenotype(call:RoutingCall){
        val request = call.requestBody(PhenotypeRequest::class.java)
        call.checkNotNull(request.phenotype)
        call.ok(PhenotypeService.findPhenotypeOntologyByPhenotype(request))
    }
    @Route
    suspend fun findGeneByPhenotype(call:RoutingCall){
        val request = call.requestBody(PhenotypeRequest::class.java)
        call.checkNotNull(request.phenotype)
        call.ok(PhenotypeService.findGeneByPhenotype(request))
    }
    @Route
    suspend fun findGeneByPhenotypeOntology(call:RoutingCall){
        val request = call.requestBody(OntologyRequest::class.java)
        call.checkNotNull(request.ontology)
        call.ok(PhenotypeService.findGeneByPhenotypeOntology(request))
    }
    @Route
    suspend fun findGeneOntologyByPhenotype(call:RoutingCall){
        val request = call.requestBody(PhenotypeRequest::class.java)
        call.checkNotNull(request.phenotype)
        call.ok(PhenotypeService.findGeneOntologyByPhenotype(request))
    }
    @Route(GET)
    suspend fun getOntologyInfo(call:RoutingCall){
        val db = call.queryParameters["db"]
        val ontologyId = call.queryParameters["ontologyId"]

        call.checkNotNull(db,ontologyId)
        val mapping = HashMap<String, String>()
        val result = FungaPlugin.dataManager.useDatabase(db!!).maps(PhenotypeOntologyTable){
            where {
                PhenotypeOntologyTable.ontologyId eq ontologyId!!
            }
        }[0]
        val qs = result["qualifiers"] as List<String>
        qs.forEach { q ->
            FungaPlugin.dataManager.useDatabase(db).maps(PhenotypeOntologyQualifierTable){
                where {
                    PhenotypeOntologyQualifierTable.qualifierId eq q
                }
            }.forEach {
                mapping[it["name"].toString()] = it["description"].toString()
            }
        }
        result["qualifiers"] = mapping
        call.ok(result)
    }
    @Route(GET)
    suspend fun getOntologyBrowser(call: RoutingCall){
        val db = call.queryParameters["db"]
        call.checkNotNull(db)
        val graph = POGraph(null)
        FungaPlugin.dataManager.useDatabase(db!!)
            .from(PhenotypeOntologyTable)
            .select()
            .forEach {
                val id = it[PhenotypeOntologyTable.ontologyId]!!
                graph.addNode(id,it.getString("name")!!)
                it[PhenotypeOntologyTable.downstream]!!.forEach {
                    graph.addLine(id,"",it)
                }
            }
        call.ok(graph.toJSONObject())
    }
}