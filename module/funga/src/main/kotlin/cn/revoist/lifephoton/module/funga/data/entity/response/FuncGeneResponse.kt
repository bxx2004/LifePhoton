package cn.revoist.lifephoton.module.funga.data.entity.response

/**
 * @author 6hisea
 * @date  2025/4/20 20:39
 * @description: None
 */
data class FuncGeneResponse(val gene:String,val phenotypes:List<PhenotypeReferences>,val summary:String) {
}