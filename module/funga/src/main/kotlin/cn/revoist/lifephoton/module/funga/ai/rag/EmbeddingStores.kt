package cn.revoist.lifephoton.module.funga.ai.rag

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.table.LiteratureTable
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore
import io.milvus.common.clientenum.ConsistencyLevelEnum
import io.milvus.param.IndexType
import io.milvus.param.MetricType
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import java.io.File
import kotlin.math.floor


/**
 * @author 6hisea
 * @date  2025/7/26 19:42
 * @description: None
 */
object EmbeddingStores {
    val lit = lazy {
        listOf(
            MilvusEmbeddingStore.builder()
                .host(FungaPlugin.properties.getProperty("milvus.url"))
                .collectionName("literature_1")      // 集合名称
                .dimension(1024)                            // 向量维度
                .indexType(IndexType.FLAT)                 // 索引类型
                .metricType(MetricType.COSINE)             // 度量类型
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)  // 一致性级别
                .autoFlushOnInsert(true)                   // 插入后自动刷新
                .idFieldName("id")                         // ID 字段名称
                .textFieldName("text")                     // 文本字段名称
                .metadataFieldName("metadata")             // 元数据字段名称
                .vectorFieldName("vector")                 // 向量字段名称
                .build()
        )
    }
    fun upload(file: File,user: Long,citation: String,title:String,visible: Boolean){
        val size = FungaPlugin.dataManager.useDatabase()
            .from(LiteratureTable)
            .select(LiteratureTable.id)
            .map { it }.size
        val page = floor(size / 100000.toDouble()).toInt()
        val document = FileSystemDocumentLoader.loadDocument(file.absolutePath)
        document.metadata().put("citation", citation)
        document.metadata().put("user", user)
        document.metadata().put("title", title)
        document.metadata().put("visible", visible.toString())
        val es = lit.value[page]
        EmbeddingStoreIngestor.ingest(document, es)
        FungaPlugin.dataManager.useDatabase().insert(LiteratureTable){
            set(LiteratureTable.user_id)
        }
    }
}