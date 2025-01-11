package cn.revoist.lifephoton.extensions.auth.data.entity.request

import kotlinx.serialization.Serializable

/**
 * @author 6hisea
 * @date  2025/1/8 19:35
 * @description: None
 */
@Serializable
data class RegisterRequest(
    val username:String,
    val password:String,
    val email:String,
    val emailCode:String,
    val company:String,
    val description:String
)