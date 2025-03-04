package cn.revoist.lifephoton.module.genome

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse

/**
 * @author 6hisea
 * @date  2025/1/18 21:38
 * @description: None
 */
@AutoUse
object Genome : Plugin() {
    override val name: String
        get() = "Genome"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "beta-1"

    override fun load() {

    }

    override fun configure() {

    }
}