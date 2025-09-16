package cn.revoist.lifephoton.module.funga.data.entity.ai

import dev.langchain4j.model.output.structured.Description

/**
 * @author 6hisea
 * @date  2025/7/9 16:55
 * @description: None
 */
@Description("表型关联分析结果")
class GeneResult {
    @Description("每个有关联的表型和其对应的推理过程")
    val result: Map<String, String> = mapOf()
    @Description("对本次分析简短的科研总结")
    val summary : String = ""
}