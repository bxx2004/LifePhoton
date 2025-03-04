package cn.revoist.lifephoton.module.genome.pages

import cn.revoist.lifephoton.module.genome.Genome
import cn.revoist.lifephoton.module.genome.data.table.GenomeSpeciesInfo
import cn.revoist.lifephoton.plugin.data.bind
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import io.ktor.server.routing.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where

/**
 * @author 6hisea
 * @date  2025/1/20 09:57
 * @description: None
 */
@RouteContainer("genome","genome-info")
object GenomeInfo{
    @Route("GET")
    @Api("获取物种的摘要",[
        Param("species","物种名称"),
        Param("version","个体版本")
    ])
    suspend fun getSummaryByFullSpecies(call: RoutingCall){
        call.checkParameters("species","version")
        val species = call.queryParameters["species"]!!
        val version = call.queryParameters["version"]!!
        call.ok(
            Genome.dataManager.useDefaultDatabase()
                .maps(cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics, cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics.id) {
                    where{
                        (cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics.speciesName eq species) and (cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics.assembledVersion eq version)
                    }
                }
            [0]
        )
    }
    @Route("GET")
    suspend fun getAllFullSpecies(call: RoutingCall){
        val result = HashMap<String,ArrayList<String>>()
        Genome.dataManager.useDefaultDatabase()
            .bind(cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics, cn.revoist.lifephoton.module.genome.data.entity.mapper.FullSpecies::class.java){
                select(cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics.speciesName, cn.revoist.lifephoton.module.genome.data.table.GenomeSummaryStatistics.assembledVersion)
            }.forEach {
                if (result[it.species] == null) result[it.species] = ArrayList()
                result[it.species]!!.add(it.version)
            }
        call.ok(result)
    }
    @Route("GET")
    suspend fun getGenomeLocation(call: RoutingCall){
        val result = ArrayList<List<Any>>()
        Genome.dataManager.useDefaultDatabase()
            .maps(GenomeSpeciesInfo, GenomeSpeciesInfo.id)
            .forEach { row ->
                val cache = ArrayList<Any>()
                cache.add((row["location"] as List<Double>)[0])
                cache.add((row["location"] as List<Double>)[1])
                cache.add(row["display_name"]!!)
                result.add(cache)
            }
        call.ok(result)
    }
    @Route("GET")
    suspend fun getFullSpeciesInfo(call: RoutingCall){
        call.checkParameters("species","version")
        val species = call.queryParameters["species"]!!
        val version = call.queryParameters["version"]!!

        call.ok(
            Genome.dataManager.useDefaultDatabase()
                .maps(GenomeSpeciesInfo, GenomeSpeciesInfo.id){
                    where{
                        (GenomeSpeciesInfo.speciesName eq species) and (GenomeSpeciesInfo.assembledVersion eq version)
                    }
                }[0]
        )
    }
}