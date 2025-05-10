package cn.revoist.lifephoton.module.funga.services

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.core.MilvusDatabase.search
import cn.revoist.lifephoton.module.funga.data.entity.request.OntologyRequest
import cn.revoist.lifephoton.module.funga.data.entity.request.PhenotypeRequest
import cn.revoist.lifephoton.module.funga.data.table.GeneOntologyTable
import cn.revoist.lifephoton.module.funga.data.table.GenePhenotypeTable
import cn.revoist.lifephoton.module.funga.data.table.PhenotypeOntologyTable
import cn.revoist.lifephoton.module.funga.tools.tryMapping
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.data.processor.MergeData
import cn.revoist.lifephoton.plugin.data.processor.join
import io.ktor.server.routing.*
import org.ktorm.dsl.eq
import org.ktorm.dsl.where

/**
 * @author 6hisea
 * @date  2025/4/19 11:55
 * @description: None
 */
object PhenotypeService {
    fun findPhenotypeOntologyByPhenotype(request:PhenotypeRequest): List<MergeData<List<Map<String, Any>>>> {
        return PhenotypeOntologyTable.search(
            request.dbs(),
            hashMapOf(
                Pair("description",request.phenotype)
            ),request.topK
            )
    }
    fun findGeneByPhenotype(request: PhenotypeRequest): List<MergeData<List<Map<String, Any>>>> {
        return GenePhenotypeTable.search(request.dbs(),hashMapOf(
            Pair("phenotype",request.phenotype)
        ),request.topK).tryMapping()
    }
    fun findGeneByPhenotypeOntology(request: OntologyRequest): List<MergeData<List<HashMap<String, Any?>>>> {
        return join(request.dbs()){db->
            FungaPlugin.dataManager.useDatabase(db)
                .maps(GenePhenotypeTable,GenePhenotypeTable.id){
                    where {
                        GenePhenotypeTable.phenotypeOntology eq request.ontology
                    }
                }.tryMapping(db)
        }
    }
    fun findGeneOntologyByPhenotype(request: PhenotypeRequest): List<Map<String, Any>> {
        return GeneOntologyTable.search("funga",
            hashMapOf(Pair("term",request.phenotype)),request.topK
            )
    }
}