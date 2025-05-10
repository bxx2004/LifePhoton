package cn.revoist.lifephoton.module.funga.ai.chat

import cn.revoist.lifephoton.module.funga.ai.ModelStore
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V

/**
 * @author 6hisea
 * @date  2025/4/20 15:01
 * @description: None
 */
interface AnalysisAssistant {
    @UserMessage("""
你是一个严谨的表型组学专家，专门判断两组表型描述之间的包含关系。请严格遵守以下规则：

1. 任务要求：
- 判断第一组中的每个表型描述是否都能在第二组中找到对应项
- 对应标准包括：完全匹配、等效匹配、语义关联或生物过程关联性
- 必须严格遵循所有规则，特别是第5和第7条

2. 输出规则：
- 只有当第一组的所有描述都能在第二组中找到对应时，才返回匹配项
- 若有任何一个第一组的描述无法匹配，必须返回"无"
- 返回格式必须使用第二组中的原始表述形式
- 禁止任何形式的编造或推测

3. 特别注意：
- 规则5和规则7是绝对要求：必须确保第一组100%被第二组包含
- 不要自行放宽标准
- 不要添加任何解释性文字

4. 示例：
输入：
第一组：heat;growth
第二组：heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase;A cycle of DNA replication and cell division via mitosis
输出：
heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase

当前任务：
第一组：{{inputPhenotype}}
第二组：{{dbPhenotype}}
    """)
    fun isPhenotypeRelatedAll(@V("inputPhenotype") inputPhenotype:List<String>, @V("dbPhenotype") dbPhenotype:List<String>):List<String>
    @UserMessage("""
作为表型组学精确匹配分析器，执行严格包含关系判断：
- 仅当第一组术语被第二组描述时（如"growth"⊆"filamentous growth"），输出第二组中与该术语关联的描述项
- 保留第二组原始顺序和完整表述，剔除无关联项
- 绝对禁止任何信息增补、删减或格式变动
- 输出结果为纯分号分隔文本，无附加内容
- 关联规则为:等效匹配、全量关联、完整匹配和是否存在生物学之间的关系
- 务必注意关联规则，一切可能存在的生物学关系全部列出
- 不要添加任何解释性文字
- 不要自行放宽标准
标准示例：
输入：
  第一组：heat;growth
  第二组：heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase;化学抵抗性
输出：
  heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase
当前任务：
第一组：{{inputPhenotype}}
第二组：{{dbPhenotype}}
    """)
    fun isPhenotypeRelatedAny(@V("inputPhenotype") inputPhenotype:List<String>, @V("dbPhenotype") dbPhenotype:List<String>):List<String>

    companion object{
        val INSTANCE = AiServices.builder(AnalysisAssistant::class.java)
            .chatLanguageModel(ModelStore.deepSeekV3)
            .build()
    }
}