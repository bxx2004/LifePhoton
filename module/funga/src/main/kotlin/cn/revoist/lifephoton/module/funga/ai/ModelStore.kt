package cn.revoist.lifephoton.module.funga.ai

import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiStreamingChatModel


/**
 * @author 6hisea
 * @date  2025/4/20 15:01
 * @description: None
 */
object ModelStore {
    var deepSeekV3 = OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-chat")
        .temperature(1.0)
        .apiKey("sk-57b14de157dd400193bc0a24b1165325")
        .build()
    var deepSeekR1 = OpenAiChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-reasoner")
        .temperature(1.0)
        .apiKey("sk-57b14de157dd400193bc0a24b1165325")
        .build()
    var chatGLMV4 = OpenAiChatModel.builder()
        .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
        .modelName("glm-4-plus")
        .temperature(1.0)
        .apiKey("c8fba303e2d621ca854ccdc3922dbd8b.EdGphBg8gXYhfa2x")
        .build()
    var deepSeekV3Chat = OpenAiStreamingChatModel.builder()
        .baseUrl("https://api.deepseek.com")
        .modelName("deepseek-chat")
        .temperature(1.0)
        .apiKey("sk-57b14de157dd400193bc0a24b1165325")
        .build()
    var chatGLMV4Chat = OpenAiStreamingChatModel.builder()
        .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
        .modelName("glm-4-plus")
        .temperature(1.0)
        .apiKey("c8fba303e2d621ca854ccdc3922dbd8b.EdGphBg8gXYhfa2x")
        .build()
}