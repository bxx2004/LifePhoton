package cn.revoist.lifephoton.module.funga.ai.assistant

import cn.revoist.lifephoton.module.funga.ai.ModelStore
import cn.revoist.lifephoton.module.funga.data.entity.ai.GeneResult
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
- 禁止主观推测。
- 禁止修改第二组原始描述（包括大小写、标点）。

输出规则
- 匹配条件
第一组中所有描述都与第二组存在关联。
输出的推理过程无需举例和参考文献，使用英文输出。不用提到其他表型，只需要输出关注的表型推断过程，使用SCI科研论文方式简短描述。
示例演示
- 输入
第一组：heat;growth
第二组：heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase;化学抵抗增加;性别改变
- 分析逻辑
"heat" → 通过因果关联匹配"Heat sensitivity: increased"（PMID 123456）
"heat" → 层级关联"Innate thermotolerance: increased"（GO:0009266）
"growth" → 直接包含"filamentous growth"和"Growth in liquid culture..."
当前任务
第一组：{{inputPhenotype}}
第二组：{{dbPhenotype}}
    """)
    fun isPhenotypeRelatedAll(@V("inputPhenotype") inputPhenotype:List<String>, @V("dbPhenotype") dbPhenotype:List<String>):GeneResult
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
- 禁止主观推测。
- 禁止修改第二组原始描述（包括大小写、标点）。

输出规则
- 匹配条件
第一组中至少一个描述与第二组存在关联。
输出的推理过程无需举例和参考文献，使用英文输出。不用提到其他表型，只需要输出关注的表型推断过程，使用SCI科研论文方式简短描述。
示例演示
- 输入
第一组：heat;growth
第二组：heat sensitivity: increased;innate thermotolerance: increased;filamentous growth;Growth in liquid culture during the post-diauxic phase;化学抵抗增加;性别改变
- 分析逻辑
"heat" → 通过因果关联匹配"Heat sensitivity: increased"（PMID 123456）
"heat" → 层级关联"Innate thermotolerance: increased"（GO:0009266）
"growth" → 直接包含"filamentous growth"和"Growth in liquid culture..."
当前任务
第一组：{{inputPhenotype}}
第二组：{{dbPhenotype}}
    """)
    fun isPhenotypeRelatedAny(@V("inputPhenotype") inputPhenotype:List<String>, @V("dbPhenotype") dbPhenotype:List<String>):GeneResult

    companion object{
        val INSTANCE = AiServices.builder(AnalysisAssistant::class.java)
            .chatLanguageModel(ModelStore.deepSeekV3)
            .build()
    }
}
