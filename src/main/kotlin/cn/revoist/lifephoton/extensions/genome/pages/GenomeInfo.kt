package cn.revoist.lifephoton.extensions.genome.pages

import cn.revoist.lifephoton.extensions.genome.Genome
import cn.revoist.lifephoton.extensions.genome.data.table.GenomeSummaryStatisticsTable
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.route.*
import cn.revoist.lifephoton.plugin.route.Route
import io.ktor.server.routing.*
import org.ktorm.dsl.*

/**
 * @author 6hisea
 * @date  2025/1/20 09:57
 * @description: None
 */
@Gateway("genome","genome-info")
object GenomeInfo{
    @Route("get")
    suspend fun getSummaryByFullSpecies(call: RoutingCall){
        call.checkParameters("species","version")
        val species = call.queryParameters["species"]!!
        val version = call.queryParameters["version"]!!
        call.ok(
            Genome.dataManager.useDefaultDatabase()
                .maps(GenomeSummaryStatisticsTable,GenomeSummaryStatisticsTable.id) {
                    where{
                        (GenomeSummaryStatisticsTable.speciesName eq species) and (GenomeSummaryStatisticsTable.assembledVersion eq version)
                    }
                }
        )
    }
}