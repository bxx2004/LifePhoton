package cn.revoist.lifephoton.module.funga.data.entity.request

/**
 * @author 6hisea
 * @date  2025/4/13 19:48
 * @description: None
 */
class GeneListWithDatabaseRequest : WithDatabasesRequest() {
    lateinit var genes:List<String>
}