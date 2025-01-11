package cn.revoist.lifephoton.extensions.filem.data.table

import cn.revoist.lifephoton.extensions.filem.data.entity.FileMetaEntity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/10 12:16
 * @description: None
 */
object FileMetaTable : Table<FileMetaEntity>("file_meta") {
    var id = long("id").bindTo { it.id }
    var path = varchar("path").bindTo { it.path }
    var expiredTime = long("expired_time").bindTo { it.expiredTime }
    var userId = long("user_id").bindTo { it.userId }
    var lock = boolean("lock").bindTo { it.lock }
}