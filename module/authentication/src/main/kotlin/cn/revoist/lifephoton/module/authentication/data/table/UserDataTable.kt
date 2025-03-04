package cn.revoist.lifephoton.module.authentication.data.table

import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/1/8 11:56
 * @description: None
 */
object UserDataTable :Table<UserDataEntity>("user_data") {
    var username = varchar("username").bindTo { it.username }
    var password = varchar("password").bindTo { it.password }
    var email = varchar("email").bindTo { it.email }
    var id = long("id").bindTo { it.id }
    var group = varchar("group").bindTo { it.group }
    var permissions = obj<List<String>>("permissions").bindTo { it.permissions }
    var accessToken = varchar("access_token").bindTo { it.accessToken }
    var refreshToken = varchar("refresh_token").bindTo { it.refreshToken }
    var data = obj<HashMap<String,Any>>("cn/revoist/lifephton/authentication/dataevoist/lifephton/authentication/data").bindTo { it.data }
    var avatar = varchar("avatar").bindTo { it.avatar }
}