package cn.revoist.lifephoton.module.authentication.data.entity.response

import kotlinx.serialization.Serializable

/**
 * @author 6hisea
 * @date  2025/1/8 16:04
 * @description: None
 */
@Serializable
data class LoginResponse(val accessToken: String, val refreshToken: String) {

}