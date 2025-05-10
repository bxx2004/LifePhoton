package cn.revoist.lifephoton.module.funga.data.table

import cn.revoist.lifephoton.module.funga.data.table.type.Source
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 11:50
 * @description: None
 */
object GeneTable : Table<Nothing>("gene") {
    val id = int("id")
    val fungaId = varchar("funga_id")
    val symbol = varchar("symbol")
    val name = varchar("name")
    val description = varchar("description")
    val otherId = obj<List<String>>("other_id")
    val type = varchar("type")
    val source = obj<Source>("source")
    val dnaSequence = varchar("dna_sequence")
    val polypeptideSequence = varchar("polypeptide_sequence")
}