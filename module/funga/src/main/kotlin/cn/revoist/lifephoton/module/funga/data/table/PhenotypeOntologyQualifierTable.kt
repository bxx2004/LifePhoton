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
object PhenotypeOntologyQualifierTable : Table<Nothing>("phenotype_ontology_qualifiers"){
        val id = int("id").primaryKey()
        val qualifierId = varchar("qualifier_id")
        val name = varchar("name")
        val description = varchar("description")
        val source = obj<Source>("source")
        val upstream = varchar("upstream")
        val downstream = obj<List<String>>("downstream")
}