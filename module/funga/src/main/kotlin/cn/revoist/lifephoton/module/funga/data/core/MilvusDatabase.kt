package cn.revoist.lifephoton.module.funga.data.core

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.ai.embedding.SentencesEmbedding
import cn.revoist.lifephoton.module.funga.data.table.*
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.data.processor.MergeData
import cn.revoist.lifephoton.plugin.data.processor.join
import cn.revoist.lifephoton.plugin.data.sqltype.ObjectType
import io.milvus.v2.client.ConnectConfig
import io.milvus.v2.client.MilvusClientV2
import io.milvus.v2.common.DataType
import io.milvus.v2.common.IndexParam
import io.milvus.v2.service.collection.request.AddFieldReq
import io.milvus.v2.service.collection.request.CreateCollectionReq
import io.milvus.v2.service.collection.request.HasCollectionReq
import io.milvus.v2.service.index.request.CreateIndexReq
import io.milvus.v2.service.vector.request.AnnSearchReq
import io.milvus.v2.service.vector.request.HybridSearchReq
import io.milvus.v2.service.vector.request.data.BaseVector
import io.milvus.v2.service.vector.request.data.FloatVec
import io.milvus.v2.service.vector.request.ranker.WeightedRanker
import org.ktorm.schema.Table
import org.ktorm.schema.VarcharSqlType
import java.util.*

/**
 * @author 6hisea
 * @date  2025/4/16 12:42
 * @description: None
 */
object MilvusDatabase {
    private val config = ConnectConfig.builder()
        .uri(FungaPlugin.option("milvus-url"))
        .username(FungaPlugin.option("milvus-username"))
        .password(FungaPlugin.option("milvus-password"))
        .token(FungaPlugin.option("milvus-token"))
        .build()
    private lateinit var client:MilvusClientV2

    fun init(){
        client = MilvusClientV2(config)
        val onlineDBs = FungaPlugin.dataManager.useDatabase()
            .maps(DBInfoTable, DBInfoTable.id)
            .map {
                it["name"] as String
            }
        onlineDBs.forEach {
            GenePhenotypeTable.createMilvusDatabaseIfNotExists(it,arrayListOf("phenotype"))
            PhenotypeOntologyTable.createMilvusDatabaseIfNotExists(it,arrayListOf("description"))
            GeneTable.createMilvusDatabaseIfNotExists(it,arrayListOf("dna_sequence","polypeptide_sequence","description"))
            PhenotypeOntologyQualifierTable.createMilvusDatabaseIfNotExists(it,arrayListOf("description"))

        }
        GeneOntologyTable.createMilvusDatabaseIfNotExists("funga",arrayListOf("term"))
    }

    private fun createCollectionIfNotExists(name: String, schema: CreateCollectionReq.CollectionSchema, vector_name: List<String>){
        val hasGOCollection = client.hasCollection(
            HasCollectionReq.builder().collectionName(name).build()
        )
        if (!hasGOCollection){
            client.createCollection(
                CreateCollectionReq.builder()
                    .collectionName(name)
                    .collectionSchema(schema)
                    .build()
            )
            vector_name.forEach {
                client.createIndex(
                    CreateIndexReq.builder().collectionName(name).indexParams(
                        listOf(
                            IndexParam.builder().metricType(IndexParam.MetricType.COSINE)
                                .indexType(IndexParam.IndexType.AUTOINDEX).fieldName(it + "_vector").build()
                        )
                    ).build()
                )
            }
        }
    }
    fun search(dbName:String, collectionName:String, sentences: Map<String, String>, topK:Int = 100): List<Map<String, Any>>{

        var map : ArrayList<Map<String, Any>> = ArrayList()

        val reqs = ArrayList<AnnSearchReq>()
        sentences.forEach {col,con->
            reqs.add(
                AnnSearchReq.builder()
                .vectorFieldName(col + "_vector")
                .vectors(Collections.singletonList(FloatVec(SentencesEmbedding.embedding(con))) as List<BaseVector>?)
                .topK(topK)
                .build())
        }

        client.hybridSearch(
            HybridSearchReq.builder()
                .collectionName(dbName+ "_" +collectionName)
                .searchRequests(reqs)
                .ranker(WeightedRanker(arrayListOf(1.0F)))
                .topK(topK)
                .outFields(Collections.singletonList("*"))
                .build()
        ).searchResults.forEach {
            it.forEach { re->
                re.entity["score"] = re.score
                val iterator = re.entity.keys.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().contains("_vector")) {
                        iterator.remove()
                    }
                }

                map.add(re.entity)
            }
        }
        return map
    }
    fun Table<*>.createMilvusDatabaseIfNotExists(dbName: String, vector_name: List<String>){
        var schema = client.createSchema()
        vector_name.forEach {
            schema.addField(AddFieldReq.builder().fieldName(it+"_vector").dataType(DataType.FloatVector).dimension(1024).build())
        }
        this.columns.forEach {
            if (it.name == "id"){
                schema.addField(AddFieldReq.builder().fieldName(it.name).dataType(DataType.VarChar).isPrimaryKey(true).autoID(true).build())
            }else{
                when(it.sqlType){
                    is VarcharSqlType ->{
                        schema.addField(AddFieldReq.builder().fieldName(it.name).dataType(DataType.VarChar).build())
                    }
                    is ObjectType -> {
                        schema.addField(AddFieldReq.builder().fieldName(it.name).dataType(DataType.JSON).build())
                    }
                }
            }
        }
        createCollectionIfNotExists(dbName+"_"+tableName,schema,vector_name)
    }
    fun Table<*>.search(dbName:String, sentences: Map<String, String>, topK:Int = 100): List<Map<String, Any>> {
        return search(dbName,tableName,sentences,topK)
    }
    fun Table<*>.search(dbName:List<String>, sentences: Map<String, String>, topK:Int = 100): List<MergeData<List<Map<String, Any>>>> {
        return join(dbName){
            search(it,tableName,sentences,topK)
        }
    }
}
