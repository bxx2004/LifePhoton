package cn.revoist.lifephoton.module.funga

import cn.revoist.lifephoton.module.funga.data.core.MilvusDatabase
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse

/**
 * @author 6hisea
 * @date  2025/4/13 11:24
 * @description: None
 */
@AutoUse
object FungaPlugin : Plugin(){
    override val name: String
        get() = "FUNGA"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "beta-1"

    override fun load() {
        MilvusDatabase.init()
    }

    override fun configure() {
        optional("embedding-url","http://localhost:11434")
        optional("milvus-url","https://in03-e97b3b8ec4edfce.serverless.gcp-us-west1.cloud.zilliz.com")
        optional("milvus-username","db_e97b3b8ec4edfce")
        optional("milvus-password","Ad4&.P9Bn<}cL2sh")
        optional("milvus-token","874af09222ab39fe485158431056f13466cbd7c199f6f9739413900f33f588340a4fbc76c466a33a63f7e6fd84d6d143e5f3dfa8")
    }
}