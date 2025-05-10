package cn.revoist.lifephoton.module.funga.data.entity.request

/**
 * @author 6hisea
 * @date  2025/4/13 19:48
 * @description: None
 */
class DoubleGeneWithDatabaseRequest : WithDatabasesRequest() {
    lateinit var gene1:String
    var gene2:String? = null
}