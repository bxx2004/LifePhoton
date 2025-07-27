package cn.revoist.lifephoton.module.funga.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 11:50
 * @description: None
 */
object LiteratureTable : Table<Nothing>("literatures") {
    val id = int("id").primaryKey()
    val user_id = varchar("user_id")
    val title = varchar("title")
    val visible = boolean("visible")
    val citation = varchar("citation")
}