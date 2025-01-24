package cn.revoist.lifephoton.extensions.genome.data.table

import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/21 17:38
 * @description: None
 */
object GenomeSummaryStatistics : Table<Nothing>("genome_summary_statistics") {
    val id = long("id").primaryKey()
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
    var totalAssemblySize = varchar("total_assembly_size")
    var N50Contigbp = varchar("n50_contig_bp")
    var N90Contigbp = varchar("n90_contig_bp")
    var N50ContigSize = varchar("n50_contig_size")
    var N90ContigSize = varchar("n90_contig_size")
    var contigSize = varchar("contig_size")
    var scaffoldSize = varchar("scaffold_size")
    var geneSize = varchar("gene_size")
    var repeatContent = varchar("repeat_content")
    var references = obj<List<String>>("references")
}