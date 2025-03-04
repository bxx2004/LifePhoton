package cn.revoist.lifephoton.module.authentication.data.entity.request

import kotlinx.serialization.Serializable

/**
 * @author 6hisea
 * @date  2025/1/8 16:00
 * @description: None
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)