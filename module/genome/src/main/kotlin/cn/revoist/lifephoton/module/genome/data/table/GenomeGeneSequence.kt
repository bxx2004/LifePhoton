package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 11:54
 * @description: None
 */
object GenomeGeneSequence : Table<Nothing>("genome_gene_sequence") {
    val id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var transcriptId = varchar("transcript_id")
    var type = varchar("type")
    var sequence = varchar("sequence")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
}