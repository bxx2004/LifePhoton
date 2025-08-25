package cn.revoist.lifephoton.plugin.route.routes.dataroute

/**
 * @author 6hisea
 * @date  2025/8/3 17:41
 * @description: None
 */
data class SelectRequest(
    val size:Int,
    val index:List<SelectIndex>
)
data class SelectIndex(
    val name:String,
    val value: Any
)