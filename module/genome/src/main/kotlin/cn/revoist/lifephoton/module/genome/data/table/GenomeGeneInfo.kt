package cn.revoist.lifephoton.module.genome.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/25 11:47
 * @description: None
 */
object GenomeGeneInfo : Table<Nothing>("genome_gene_info") {
    val id = long("id").primaryKey()
    var geneId = varchar("gene_id")
    var geneName = varchar("gene_name")
    var geneType = varchar("gene_type")
    var locus = varchar("locus")
    var length = long("length")
    var description = varchar("description")
    var chromosome = varchar("chromosome")
    var start = long("start")
    var end = long("end")
    var strand = varchar("strand")
    var longestTranscript = varchar("longest_transcript")
    var longestTranscriptLength = varchar("longest_transcript_length")
    var codingRegionInTheLongestTranscript = varchar("coding_region_in_the_longest_transcript")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
}