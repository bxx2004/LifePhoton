package cn.revoist.lifephoton.module.funga.data.entity.inneral

import kotlinx.serialization.Serializable

/**
 * @author 6hisea
 * @date  2025/7/26 13:51
 * @description: None
 */
@Serializable
data class GeoIp (
    val status: String,
    val country: String?,
    val countryCode: String?,
    val region: String?,
    val regionName: String?,
    val city: String?,
    val zip: String?,
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val isp: String?,
    val org: String?,
    val query: String?
)