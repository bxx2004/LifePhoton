package cn.revoist.lifephoton.module.funga.ai.chat

import cn.revoist.lifephoton.module.funga.ai.ModelStore
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V

/**
 * @author 6hisea
 * @date  2025/4/20 15:01
 * @description: None
 */
interface ChatAssistant {
    @SystemMessage("你是一个基因挖掘类论文生成的小助手，你的任务是生成基因表型关系论文的讨论部分")
    @UserMessage("""
        1.按照以下规则生成
         - 使用英文生成
         - 具有逻辑性和推理性
         - 内容丰富，字数不要太少
         - 其中func是功能基因，predict是预测基因，outer是不在输入列表内的基因，提一下就可以了
        2.输入数据如下：
            {{data}}
    """)
    fun generateDiscretion(@V("data") data:String):String

    companion object{
        val INSTANCE = AiServices.builder(ChatAssistant::class.java)
            .chatLanguageModel(ModelStore.deepSeekV3)
            .build()
    }
}