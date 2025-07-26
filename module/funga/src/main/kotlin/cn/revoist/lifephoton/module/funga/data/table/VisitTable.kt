package cn.revoist.lifephoton.module.funga.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 11:50
 * @description: None
 */
object VisitTable : Table<Nothing>("visit") {
    val id = int("id").primaryKey()
    val ip = varchar("ip")
    val lat = double("lat")
    val lon = double("lon")
    val country = varchar("country")
    val region = varchar("region")
    val city = varchar("city")
}