package cn.revoist.lifephoton.module.genome.data.entity.request

/**
 * @author 6hisea
 * @date  2025/1/25 16:33
 * @description: None
*/
open class GeneBasicRequest {
    lateinit var geneId:String
    var speciesName:String? = null
    var assembledVersion:String? = null
}