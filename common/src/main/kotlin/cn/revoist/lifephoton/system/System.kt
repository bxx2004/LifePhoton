package cn.revoist.lifephoton.system

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse

/**
 * @author 6hisea
 * @date  2025/1/18 15:49
 * @description: None
 */
@AutoUse
object System : Plugin() {
    override val name: String
        get() = "System"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "beta-1"

    override fun load() {

    }

    override fun configure() {

    }
}