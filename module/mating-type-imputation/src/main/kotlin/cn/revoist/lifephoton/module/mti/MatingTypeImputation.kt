package cn.revoist.lifephoton.module.mti

import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse

/**
 * @author 6hisea
 * @date  2025/3/2 20:08
 * @description: None
 */
@AutoUse
object MatingTypeImputation : Plugin() {
    override val name: String
        get() = "MatingTypeImputation"
    override val id: String
        get() = "mating-type-imputation"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "beta-1"

    override fun load() {

    }
}