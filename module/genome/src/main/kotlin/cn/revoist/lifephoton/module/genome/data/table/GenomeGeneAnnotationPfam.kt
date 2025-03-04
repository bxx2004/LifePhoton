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
object GenomeGeneAnnotationPfam : Table<Nothing>("genome_gene_annotation_pfam") {
    var id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var start = long("start")
    var end = long("end")
    var pfamId = varchar("pfam_id")
    var annotation = varchar("annotation")
    var speciesName = varchar("species_name")
    var evalue = double("evalue")
    var assembledVersion = varchar("assembled_version")
}