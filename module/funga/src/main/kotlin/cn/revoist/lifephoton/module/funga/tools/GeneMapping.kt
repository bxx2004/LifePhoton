package cn.revoist.lifephoton.module.funga.tools

import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.table.GeneTable
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.data.mapsWithColumn
import cn.revoist.lifephoton.plugin.data.processor.MergeData
import org.ktorm.dsl.*

/**
 * @author 6hisea
 * @date  2025/4/13 11:33
 * @description: None
 */
fun Query.whereGene(
    gene:String
): Query{
    return where {
        (GeneTable.fungaId eq gene) or (GeneTable.symbol eq gene) or (GeneTable.otherId like "%$gene%")
    }
}

fun String.asFungaId(db:String):String{
    val r =  FungaPlugin.dataManager.useDatabase(db)
        .maps(GeneTable,GeneTable.id){
            whereGene(this@asFungaId)
        }.filter {
            it["funga_id"] == this@asFungaId || it["symbol"] == this@asFungaId || (it["other_id"] as List<String>).contains(this@asFungaId)
        }.firstOrNull()
    if (r == null) return "Not Found Funga Id"
    return r["funga_id"].toString()
}
fun List<String>.asFungaId(db:String):List<String>{
    if (isEmpty()) return arrayListOf()
    val result = arrayListOf<String>()
    forEach {
        result.add(it.asFungaId(db))
    }
    result.removeIf {
        it == "Not Found Funga Id"
    }
    return result
}

fun String.asSymbol(db:String):String{
    if (isEmpty()) return "Not Found Funga Id"
    val r =  FungaPlugin.dataManager.useDatabase(db)
        .maps(GeneTable,GeneTable.id){
            where { GeneTable.fungaId eq this@asSymbol }
        }.firstOrNull()
    if (r == null) return "Not Found Funga Id"
    return (r["symbol"]?:(r["other_id"] as List<String>)[0]).toString()
}
fun List<String>.asSymbol(db:String):Map<String,String>{
    if (isEmpty()) return hashMapOf()
    val result = hashMapOf<String,String>()
    FungaPlugin.dataManager.useDatabase(db)
        .maps(GeneTable,GeneTable.id){
            where { GeneTable.fungaId inList this@asSymbol }
        }.forEach {
            result[it["funga_id"].toString()]= (it["symbol"]?:(it["other_id"] as List<String>)[0]).toString()
            if (result[it["funga_id"].toString()] == "None"){
                result[it["funga_id"].toString()] = (it["other_id"] as List<String>)[0].toString()
            }
        }
    return result
}

fun List<MergeData<List<Map<String, Any>>>>.tryMapping():List<MergeData<List<Map<String, Any>>>>{
    return map { md->
        md.data = md.data.tryMappings(md.database) as List<Map<String, Any>>
        md
    }
}
fun List<HashMap<String,Any?>>.tryMapping(db: String):List<HashMap<String,Any?>>{
    val idList = mutableListOf<String>()
    forEach{map->
        val keys = map.keys
        keys.forEach {
            if (it.contains("gene")){
                idList.add(map[it].toString())
            }
        }
    }
    val symbols = idList.asSymbol(db)
    return map{ map->
        val keys = map.keys
        keys.forEach {
            if (it.contains("gene")){
                map[it] = symbols[map[it]]
            }
        }
        map
    }
}
fun List<Map<String,Any?>>.tryMappings(db: String):List<Map<String,Any?>>{
    val idList = mutableListOf<String>()
    forEach{map->
        val keys = map.keys
        keys.forEach {
            if (it.contains("gene")){
                idList.add(map[it].toString())
            }
        }
    }
    val symbols = idList.asSymbol(db)
    return map{ map->
        val nm = hashMapOf<String,Any?>()
        nm.putAll(map)
        val keys = nm.keys
        keys.forEach {
            if (it.contains("gene")){
                nm[it] = symbols[nm[it]]
            }
        }
        nm
    }
}
