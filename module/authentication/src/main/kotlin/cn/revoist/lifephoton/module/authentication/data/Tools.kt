package cn.revoist.lifephoton.module.authentication.data

import cn.revoist.lifephoton.module.authentication.Auth
import cn.revoist.lifephoton.module.authentication.data.table.UserDataTable
import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.*
import java.util.*

/**
 * @author 6hisea
 * @date  2025/1/8 14:55
 * @description: None
 */
object Tools {
    private val expired = HashMap<String,Long>()
    fun generateToken(): String {
        return MessageDigestUtils.sha256(UUID.randomUUID().toString())
    }
    fun checkToken(session: UserSession):Boolean {
        if (expired[session.accessToken] == null) return false
        if (System.currentTimeMillis() > expired[session.accessToken]!!) {
            if (System.currentTimeMillis() - expired[session.accessToken]!! > 1000*60*10){
                session.accessToken = ""
                session.refreshToken = ""
                return false
            }
            val user = findUserByToken(session.accessToken)!!
            if (session.refreshToken == user.refreshToken){
                val t = updateToken(user.username)
                session.accessToken = t.first
                session.refreshToken = t.second
                return true
            }
        }
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                it.accessToken eq session.accessToken
            }.count() > 0
    }
    fun comparePassword(username: String,password: String): Boolean {
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                it.username eq username
            }.filter {
                it.password eq password
            }.count() > 0
    }
    fun findUserByToken(token:String): UserDataEntity?{
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                it.accessToken eq token
            }.firstOrNull()
    }
    fun updateToken(username: String):Pair<String, String> {
        val accessToken = generateToken()
        expired[accessToken] = System.currentTimeMillis() + (30 * 60 * 1000)
        val refreshToken = generateToken()
        Auth.dataManager.useDatabase()
            .update(UserDataTable){
                set(it.accessToken, accessToken)
                set(it.refreshToken, refreshToken)
                where {
                    it.username eq username
                }
            }
        return Pair(accessToken,refreshToken)
    }
    fun hasUser(username: String,email:String): Boolean {
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                (it.username eq username) and (it.email eq email)
            }
            .count() > 0
    }
    fun addUser(user: UserDataEntity){
        Auth.dataManager.useDatabase().sequenceOf(UserDataTable).add(user)
    }
    fun generateCode():String{
        var result = ""
        val number = arrayListOf(1,2,3,4,5,6,7,8,9,0)
        for (i in 0 until 6) {
            result += number.random().toString()
        }
        return result
    }
    fun getUser(username: String): UserDataEntity?{
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                it.username eq username
            }.firstOrNull()
    }
    fun getUserById(id: Long): UserDataEntity?{
        return Auth.dataManager.useDatabase()
            .sequenceOf(UserDataTable)
            .filter {
                it.id eq id
            }.firstOrNull()
    }
}