package cn.revoist.lifephoton.module.funga.ai.assistant

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
    @SystemMessage("你的功能是根据给定输入匹配数据库名称")
    @UserMessage("""
        给定输入: {{data}}
        数据库列表: {{dbList}}
        请直接返回匹配到的数据库名称，如果没有匹配随机返回一个。
    """)
    fun findDatabase(@V("data") data:String, @V("dbList")dbList: List<String>):String

    companion object{
        val INSTANCE = AiServices.builder(ChatAssistant::class.java)
            .chatLanguageModel(ModelStore.deepSeekV3)
            .build()
    }
}