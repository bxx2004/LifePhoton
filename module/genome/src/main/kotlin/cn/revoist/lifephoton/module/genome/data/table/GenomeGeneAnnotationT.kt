package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 12:00
 * @description: None
 */
object GenomeGeneAnnotationT : Table<Nothing>("genome_gene_annotation_t") {
    var id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var tsaccver = varchar("tsaccver")
    var pident = double("pident")
    var length = long("length")
    var mismatch = long("mismatch")
    var gapOpen = long("gapopen")
    var qStart = long("q_start")
    var qEnd = long("q_end")
    var sStart = long("s_start")
    var sEnd = long("s_end")
    var bitscore = double("bitscore")
    var evalue = double("evalue")
    var annotation = varchar("annotation")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
    var source = varchar("source")
}