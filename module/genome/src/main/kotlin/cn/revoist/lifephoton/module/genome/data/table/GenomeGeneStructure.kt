package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 11:54
 * @description: None
 */
object GenomeGeneStructure : Table<Nothing>("genome_gene_structure") {
    val id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var transcriptId = varchar("transcript_id")
    var locus = varchar("locus")
    var type = varchar("type")
    var start = long("start")
    var end = long("end")
    var length = long("length")
    var strand  = varchar("strand")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
}