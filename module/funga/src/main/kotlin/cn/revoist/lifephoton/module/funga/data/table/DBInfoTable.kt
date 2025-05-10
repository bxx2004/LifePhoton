package cn.revoist.lifephoton.module.funga.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 13:00
 * @description: None
 */
object DBInfoTable : Table<Nothing>("db_info") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val type = varchar("type")
}