package cn.revoist.lifephoton.module.authentication.data.entity

import org.ktorm.entity.Entity

/**
 * @author 6hisea
 * @date  2025/1/10 12:18
 * @description: None
*/
interface UserDataEntity : Entity<UserDataEntity> {
    companion object : Entity.Factory<UserDataEntity>()
    val id: Long
    var avatar : String
    var username: String
    var email:String
    var password: String
    var accessToken: String
    var refreshToken: String
    var group:String
    var permissions:List<String>
    var data: HashMap<String,Any>
}