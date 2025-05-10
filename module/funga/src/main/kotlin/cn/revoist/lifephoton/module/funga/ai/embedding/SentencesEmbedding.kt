package cn.revoist.lifephoton.module.funga.ai.embedding

import cn.revoist.lifephoton.module.funga.FungaPlugin
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.ollama.OllamaEmbeddingModel

/**
 * @author 6hisea
 * @date  2025/4/16 13:09
 * @description: None
 */
object SentencesEmbedding {
    val model: EmbeddingModel = OllamaEmbeddingModel.builder()
        .baseUrl(FungaPlugin.option("embedding-url"))
        .modelName("mxbai-embed-large")
        .build()
    fun embedding(text:String):List<Float>{
        return model.embed(text).content().vector().toList()
    }
}