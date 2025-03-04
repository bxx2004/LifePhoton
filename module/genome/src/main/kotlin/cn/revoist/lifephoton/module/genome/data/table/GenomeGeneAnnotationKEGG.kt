package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 12:00
 * @description: None
 */
object GenomeGeneAnnotationKEGG : Table<Nothing>("genome_gene_annotation_kegg") {
    var id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var keggId = varchar("kegg_id")
    var keggSymbol = varchar("kegg_symbol")
    var keggName = varchar("kegg_name")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
}