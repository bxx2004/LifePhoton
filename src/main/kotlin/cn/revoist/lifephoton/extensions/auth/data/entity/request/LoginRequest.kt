package cn.revoist.lifephoton.extensions.auth.data.entity.request

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