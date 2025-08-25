package cn.revoist.lifephoton.module.funga.data.table.type

/**
 * @author 6hisea
 * @date  2025/8/24 15:24
 * @description: None
 */
data class AnalysisSummary(
    var species:List<String>,
    var geneCount: Int,
    var phenotypes:List<String>,
    val type:String,
    val degree:Int,
    val topK: Int
)