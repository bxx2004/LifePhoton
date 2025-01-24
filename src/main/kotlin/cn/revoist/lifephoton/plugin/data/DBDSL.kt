package cn.revoist.lifephoton.plugin.data

import cn.revoist.lifephoton.plugin.data.entity.Map
import cn.revoist.lifephoton.plugin.properties
import com.google.gson.Gson
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import java.sql.ResultSetMetaData
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.kotlinProperty

/**
 * @author 6hisea
 * @date  2025/1/22 19:09
 * @description: None
 */
fun Database.maps(table: Table<*>, vararg columns: Column<*>, func: Query.()-> Query? = {null}):List<HashMap<String,Any?>>{
    val selected = ArrayList<Column<*>>()
    table.columns.forEach {
        if (!columns.map { it.name }.contains(it.name)){
            selected.add(it)
        }
    }
    val re = ArrayList<HashMap<String,Any?>>()
    var a=  from(table).select(selected)
    a = func(a)?:a
    a.forEach { row->
        val r = HashMap<String,Any?>()
        selected.forEach {
            r[it.name] = row[it]
        }
        re.add(r)
    }
    return re
}

private fun findLabel(meta:ResultSetMetaData):List<String>{
    return (1..meta.columnCount).map { meta.getColumnLabel(it) }
}



fun <T :Table<out Any>,R>Database.bind(table:T,entity:Class<R>,func: QuerySource.()->Query):List<R>{
    val query = func(from(table))

    val result = ArrayList<R>()

    query.forEach { row ->
        val entity = entity.getConstructor().newInstance()!!
        val labels = findLabel(row.metaData)
        entity.properties().filter {
            it.kotlinProperty?.findAnnotations(Map::class)?.isNotEmpty() == true
        }.forEach { property ->
            val mapAnnotation = property.kotlinProperty!!.findAnnotations(Map::class)[0]
            val mapName = if (mapAnnotation.colName == "&empty") property.name else mapAnnotation.colName

            listOf(mapName, "${table.tableName}_$mapName").forEach { name ->
                if (labels.contains(name)) {
                    property.set(entity, row.getObject(name))
                }
            }
        }
        result.add(entity)
    }

    return result
}
