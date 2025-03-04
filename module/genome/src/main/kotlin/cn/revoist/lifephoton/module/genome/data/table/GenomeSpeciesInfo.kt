package cn.revoist.lifephoton.module.genome.data.table

import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/23 13:36
 * @description: None
 */
object GenomeSpeciesInfo : Table<Nothing>("genome_species_info") {
    val id = long("id").primaryKey()
    var location = obj<List<Double>>("location")
    var speciesName = varchar("species_name")
    var assembledVersion = varchar("assembled_version")
    var displayName = varchar("display_name")
    var description = varchar("description")
    var imagePath = varchar("image_path")
    var englishName = varchar("english_name")
    var chineseName = varchar("chinese_name")
}