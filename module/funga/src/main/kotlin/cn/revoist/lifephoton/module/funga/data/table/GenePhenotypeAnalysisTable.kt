package cn.revoist.lifephoton.module.funga.data.table

import cn.revoist.lifephoton.module.funga.data.entity.inneral.AnalysisResult
import cn.revoist.lifephoton.module.funga.data.table.type.AnalysisSummary
import cn.revoist.lifephoton.plugin.data.sqltype.obj
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * @author 6hisea
 * @date  2025/8/24 15:22
 * @description: None
 */
object GenePhenotypeAnalysisTable : Table<Nothing>("gene_phenotype_analysis"){
    val id = int("id").primaryKey()
    val analysis_id = varchar("analysis_id")
    val user_id = long("user_id")
    val date = varchar("date")
    val result = obj<AnalysisResult>("result")
    val summary = obj<AnalysisSummary>("summary")
}