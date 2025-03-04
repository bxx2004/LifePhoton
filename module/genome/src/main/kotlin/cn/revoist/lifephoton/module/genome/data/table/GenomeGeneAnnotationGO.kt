package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 12:00
 * @description: None
 */
object GenomeGeneAnnotationGO : Table<Nothing>("genome_gene_annotation_go") {
    var id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var goId = varchar("go_id")
    var type = varchar("type")
    var annotation = varchar("annotation")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
}