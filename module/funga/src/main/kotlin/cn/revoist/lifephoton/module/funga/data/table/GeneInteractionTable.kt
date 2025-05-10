package cn.revoist.lifephoton.module.funga.data.table

import cn.revoist.lifephoton.module.funga.data.table.type.Source
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/4/13 12:44
 * @description: None
 */
object GeneInteractionTable : Table<Nothing>("gene_gene"){
    val id = int("id")
    // gene1: 基因1标识符
    val gene1 = varchar("gene1")

    // gene2: 基因2标识符
    val gene2 = varchar("gene2")

    // type: 互作类型
    val type = varchar("type")

    // source: 数据来源
    val source = obj<Source>("source")

    // references: 参考文献
    val references = obj<List<String>>("references")
}