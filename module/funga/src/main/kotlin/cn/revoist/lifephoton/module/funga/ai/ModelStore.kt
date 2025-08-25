package cn.revoist.lifephoton.module.funga.ai

import cn.revoist.lifephoton.module.funga.FungaPlugin
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiStreamingChatModel


/**
 * @author 6hisea
 * @date  2025/4/20 15:01
 * @description: None
 */
object ModelStore {
    val DeepSeekKey = FungaPlugin.getConfig("ai.deepseek","xxxxxx")
    val GLMKey = FungaPlugin.getConfig("ai.chatglm","xxxxxx")
    var deepSeekV3 = OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-chat")
        .temperature(1.0)
        .apiKey(DeepSeekKey)
        .build()
    var deepSeekV3JSON = OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-chat")
        .temperature(1.0)
        .apiKey(DeepSeekKey)
        .strictJsonSchema(true)
        .build()
    var deepSeekR1 = OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-reasoner")
        .temperature(1.0)
        .apiKey(DeepSeekKey)
        .build()
    var chatGLMV4 = OpenAiChatModel.builder()
        .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
        .modelName("glm-4-plus")
        .temperature(1.0)
        .apiKey(GLMKey)
        .build()
    var deepSeekV3Chat = OpenAiStreamingChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-chat")
        .temperature(1.0)
        .apiKey(DeepSeekKey)
        .build()
    var chatGLMV4Chat = OpenAiStreamingChatModel.builder()
        .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
        .modelName("glm-4-plus")
        .temperature(1.0)
        .apiKey(GLMKey)
        .build()
}