package cn.revoist.lifephoton.module.funga.data.entity.ai

import dev.langchain4j.model.output.structured.Description

/**
 * @author 6hisea
 * @date  2025/7/9 16:55
 * @description: None
 */
@Description("功能基因的分析结果")
class FuncGeneResult {
    @Description("基因名称")
    val gene:String = ""
    @Description("表型列表")
    val phenotypes: List<String> = arrayListOf()
}