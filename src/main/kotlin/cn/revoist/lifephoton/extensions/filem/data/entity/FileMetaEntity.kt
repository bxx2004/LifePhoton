package cn.revoist.lifephoton.extensions.filem.data.entity

import org.ktorm.entity.Entity

/**
 * @author 6hisea
 * @date  2025/1/10 12:17
 * @description: None
 */
interface FileMetaEntity:Entity<FileMetaEntity> {
    companion object : Entity.Factory<FileMetaEntity>()
    val id: Long
    var path: String
    var expiredTime:Long
    var userId:Long
    var lock:Boolean
}