package cn.revoist.lifephoton.plugin.data

import org.ktorm.dsl.and
import org.ktorm.schema.ColumnDeclaring

/**
 * @author 6hisea
 * @date  2025/1/25 18:10
 * @description: None
 */
class Where{
    private val data = ArrayList<ColumnDeclaring<Boolean>>()
    fun appendIf(con:()->Boolean,func:()->ColumnDeclaring<Boolean>) {
        if (con()){
            data.add(func())
        }
    }
    fun appendIf(con:Boolean,func:()->ColumnDeclaring<Boolean>) {
        if (con){
            data.add(func())
        }
    }
    fun append(func:()->ColumnDeclaring<Boolean>) {
        data.add(func())
    }
    fun get(): ColumnDeclaring<Boolean> {
        return data.reduce { a, b -> a and b }
    }
}
fun buildWhere(func: Where.() -> Unit):ColumnDeclaring<Boolean> {
    val r = Where()
    func(r)
    return r.get()
}