package cn.revoist.lifephoton.module.authentication.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/8/24 15:49
 * @description: None
 */
object MessageTable : Table<Nothing>("message") {
    val id = long("id").primaryKey()
    val title = varchar("title")
    val subtitle = varchar("subtitle")
    val from = long("from")
    val to = long("to")
    val content = varchar("content")
    val read = boolean("read")
    val timestamp = long("timestamp")
}