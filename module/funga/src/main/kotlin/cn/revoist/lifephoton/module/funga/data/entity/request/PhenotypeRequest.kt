package cn.revoist.lifephoton.module.funga.data.entity.request

/**
 * @author 6hisea
 * @date  2025/4/19 11:54
 * @description: None
 */
class PhenotypeRequest : WithDatabasesRequest(){
    lateinit var phenotype:String
    var topK:Int = 20
}