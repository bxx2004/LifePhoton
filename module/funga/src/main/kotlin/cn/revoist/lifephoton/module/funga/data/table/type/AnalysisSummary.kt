package cn.revoist.lifephoton.module.funga.data.table.type

/**
 * @author 6hisea
 * @date  2025/8/24 15:24
 * @description: None
 */
data class AnalysisSummary(
    var species:List<String>,
    var genes: List<String>,
    var phenotypes:List<String>,
    val type:String,
    val degree:Int,
    val topK: Int,
    val funcGeneCount: HashMap<String,Int>,
    val degreeGeneCount: HashMap<String,HashMap<Int,Int>>,
    val extraGeneCount: HashMap<String,Int>,
    val selectedGene:List<String>,
)