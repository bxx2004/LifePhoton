package cn.revoist.lifephoton.module.authentication.data.entity.request

import kotlinx.serialization.Serializable

/**
 * @author 6hisea
 * @date  2025/8/25 15:22
 * @description: None
 */
@Serializable
data class MessageRequest(
    val title:String,
    val subtitle:String,
    val content: String,
    val users: List<Long>
)