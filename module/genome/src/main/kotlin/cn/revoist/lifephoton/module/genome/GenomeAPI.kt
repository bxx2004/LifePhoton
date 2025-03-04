package cn.revoist.lifephoton.module.genome

import cn.revoist.lifephoton.module.genome.data.table.GenomeGeneAnnotationPfam
import org.ktorm.schema.Table

/**
 * @author 6hisea
 * @date  2025/1/25 18:20
 * @description: None
*/
object GenomeAPI {
    fun useAnnotationTables():List<Table<Nothing>>{
        return arrayListOf(
            cn.revoist.lifephoton.module.genome.data.table.GenomeGeneAnnotationGO,
            cn.revoist.lifephoton.module.genome.data.table.GenomeGeneAnnotationKEGG,
            GenomeGeneAnnotationPfam,
            cn.revoist.lifephoton.module.genome.data.table.GenomeGeneAnnotationTf
        )
    }
}