package cn.revoist.lifephoton.module.funga.data.entity.inneral

/**
 * @author 6hisea
 * @date  2025/8/27 14:11
 * @description: None
 */
data class DynamicDataResult(
    val genePhenotype: DynamicGenePhenotype,
    val geneGene:DynamicGeneGene,
)

data class DynamicGenePhenotype(val gene:String,val phenotype:String)
data class DynamicGeneGene(val gene1:String,val gene2:String,val type:String)
