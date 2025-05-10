package cn.revoist.lifephoton.module.funga.data.table

import cn.revoist.lifephoton.module.funga.data.table.type.Source
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 12:55
 * @description: None
 */
object GeneOntologyTable : Table<Nothing>("go_annotations") {
    // id: 主键标识
    val id = int("id").primaryKey()
    // goId: 基因本体论标识符
    val goId = varchar("go_id")
    // term: 基因本体论术语
    val term = varchar("term")
    // source: 数据来源
    val source = obj<Source>("source")
    // references: 参考文献
    val references = obj<List<String>>("references")
}