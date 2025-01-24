package cn.revoist.lifephoton.extensions.genome.data.entity.mapper

import cn.revoist.lifephoton.plugin.data.entity.Map

/**
 * @author 6hisea
 * @date  2025/1/22 18:49
 * @description: None
 */
class FullSpecies {
    @Map("species_name")
    lateinit var species: String
    @Map("assembled_version")
    lateinit var version: String
}