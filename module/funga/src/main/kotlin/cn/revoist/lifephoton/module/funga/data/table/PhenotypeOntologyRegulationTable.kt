package cn.revoist.lifephoton.module.funga.data.table

import cn.revoist.lifephoton.module.funga.data.table.type.Source
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 12:44
 * @description: None
 */
object PhenotypeOntologyRegulationTable : Table<Nothing>("go_regulations"){
        val id = int("id").primaryKey()
        val goId1 = varchar("goid1")
        val goId2 = varchar("goid2")
        val type = varchar("type")
        val source = obj<Source>("source")
        val references = obj<List<String>>("references")
}