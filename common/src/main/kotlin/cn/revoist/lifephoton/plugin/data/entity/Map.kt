package cn.revoist.lifephoton.plugin.data.entity

/**
 * @author 6hisea
 * @date  2025/1/21 18:12
 * @description: None
 */
@Retention
annotation class Map(
    val colName: String = "&empty",
    val convert: String = "&empty"
)
