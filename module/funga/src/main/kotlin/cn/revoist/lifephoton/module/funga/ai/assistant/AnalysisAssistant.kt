package cn.revoist.lifephoton.module.funga.ai.assistant

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
角色定义
你是一个表型组学专家，擅长基于生物数据库和文献证据判断表型描述的关联性。你的任务是根据科学依据分析两组表型术语的包含关系。

判断规则
关联标准（需满足至少一项）
- 间接匹配：表型术语在生物学过程中具有功能关联（如 "芽管形成" 依赖细胞壁重塑，或 "shmoo formation" 涉及细胞壁动态调节）。
- 直接匹配：术语完全一致或为官方同义词（参考Gene Ontology、PhenoBank等数据库）。
- 功能关联：属于同一生物通路/过程（如细胞壁合成与shmoo形成均涉及酵母形态发生）。
- 层级关系：广义与狭义术语（如"growth"包含"filamentous growth"）。
- 因果关联：已知因果关系（如"heat"可引发"heat sensitivity: increased"）。
- 结构关联：涉及同一生物结构（如线粒体功能异常与呼吸链缺陷）。

禁止行为
- 禁止主观推测，无关联需返回"无"。
- 禁止修改第二组原始描述（包括大小写、标点）。

输出规则
- 匹配条件
第一组中至少一个描述与第二组存在关联。
- 输出格式
无关联 → 返回"无"。
有关联 → 严格按第二组的原始表述列出所有关联项（即使关联不同第一组术语）。
- 直接输出结果即可，无需输出推理逻辑，但你在内部必须进行推理。

示例演示
- 输入
第一组：heat;growth
第二组：heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase;化学抵抗增加;性别改变
- 分析逻辑
"heat" → 通过因果关联匹配"Heat sensitivity: increased"（PMID 123456）
"heat" → 层级关联"Innate thermotolerance: increased"（GO:0009266）
"growth" → 直接包含"filamentous growth"和"Growth in liquid culture..."
- 输出
heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase

当前任务
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
