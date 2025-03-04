package cn.revoist.lifephoton.plugin.route

/**
 * @author 6hisea
 * @date  2025/1/24 19:25
 * @description: None
 */
annotation class Api(
    val description: String,
    val params: Array<Param> = []
)
annotation class Param(val name:String,val description: String)