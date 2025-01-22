package cn.revoist.lifephoton.plugin.route

/**
 * @author 6hisea
 * @date  2025/1/22 15:56
 * @description: None
 */
annotation class Route(
    val method: String,
    val path:String = "&empty",
    val auth:Boolean = false,
    val inject:Boolean = false,
)
