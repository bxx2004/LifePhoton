package cn.revoist.lifephoton.plugin.anno

import cn.revoist.lifephoton.Booster

/**
 * @author 6hisea
 * @date  2025/1/8 13:58
 * @description: None
 */
annotation class AutoRegister(
    val pluginId:String,
    val version:Booster.SystemVersion = Booster.SystemVersion.NORMAL
)
