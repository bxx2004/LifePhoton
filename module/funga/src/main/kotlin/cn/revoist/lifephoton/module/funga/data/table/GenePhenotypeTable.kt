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
object GenePhenotypeTable : Table<Nothing>("gene_phenotype"){
        // id 主键
        val id = int("id").primaryKey()
        // 基因
        val gene = varchar("gene")
        // 表型
        val phenotype = varchar("phenotype")
        // 来源
        val source = obj<Source>("source")
        // 表型限定符
        val phenotypeQualifier = varchar("phenotype_qualifier")
        // 表型本体
        val phenotypeOntology = varchar("phenotype_ontology")
        // 参考文献
        val references = obj<List<String>>("references")
}