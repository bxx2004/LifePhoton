package cn.revoist.lifephoton.plugin.anno

import cn.revoist.lifephoton.Booster

/**
 * @author 6hisea
 * @date  2025/1/9 10:43
 * @description: None
 */
annotation class Pages(
    val pack:String,
    val version:Booster.SystemVersion = Booster.SystemVersion.NORMAL
)
