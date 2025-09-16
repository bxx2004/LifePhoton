package cn.revoist.lifephoton.module.authentication.pages

import cn.revoist.lifephoton.module.authentication.Auth
import cn.revoist.lifephoton.module.authentication.asEntity
import cn.revoist.lifephoton.module.authentication.data.entity.UserDataEntity
import cn.revoist.lifephoton.module.authentication.data.entity.request.MessageRequest
import cn.revoist.lifephoton.module.authentication.data.table.MessageTable
import cn.revoist.lifephoton.module.authentication.getUser
import cn.revoist.lifephoton.module.authentication.isLogin
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.match
import cn.revoist.lifephoton.plugin.paging
import cn.revoist.lifephoton.plugin.requestBody
import cn.revoist.lifephoton.plugin.route.Api
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.POST
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.checkParameters
import cn.revoist.lifephoton.plugin.route.error
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.server.routing.RoutingCall
import org.ktorm.dsl.and
import org.ktorm.dsl.asIterable
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.neq
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.dsl.update
import org.ktorm.dsl.where

/**
 * @author 6hisea
 * @date  2025/8/25 14:58
 * @description: None
 */
@RouteContainer("auth","message")
object Message {
    @Route(GET)
    @Api("未读消息数量")
    suspend fun unreadCount(call: RoutingCall){
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            call.ok(Auth.dataManager.useDatabase()
                .maps(MessageTable){
                    where {
                        (MessageTable.read eq false) and ((MessageTable.to eq user.id) or (MessageTable.to eq -1))
                    }
                }.count())
        }.default {
            error("Not login")
        }
    }
    @Route(GET)
    @Api("get消息")
    suspend fun getMessage(call: RoutingCall){
        call.checkParameters("id")
        val id = call.queryParameters["id"]!!.toLong()
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            if (user.hasMessage(id)){
                call.ok(Auth.dataManager.useDatabase()
                    .maps(MessageTable){
                        where {
                            MessageTable.id eq id
                        }
                    }.first())
            }else{
                error("Not has message")
            }
        }.default {
            error("Not login")
        }
    }
    @Route(POST)
    @Api("发送消息")
    suspend fun sendMessage(call: RoutingCall){
        val req = call.requestBody(MessageRequest::class.java)
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            if (user.group == "admin"){
                MessageTable.sendMessage(
                    user.id,
                    req.users,
                    req.title,
                    req.subtitle,
                    req.content
                )
                call.ok("ok")
            }
        }.default {
            error("Not login")
        }
    }
    @Route(GET)
    @Api("删除信息")
    suspend fun deleteMessage(call: RoutingCall){
        call.checkParameters("id")
        val id = call.parameters["id"]!!.toLong()
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            if (user.hasMessage(id)){
                Auth.dataManager.useDatabase()
                    .delete(MessageTable){
                        MessageTable.id eq id and (MessageTable.to neq -1)
                    }
                call.ok("ok")
            }else{
                error("Not has message")
            }
        }.default {
            error("Not login")
        }
    }
    @Route(GET)
    @Api("aaa")
    suspend fun getMessages(call: RoutingCall){
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            call.paging(Auth.dataManager,Auth.dataManager.useDatabase()
                .maps(MessageTable, MessageTable.content){
                    where {
                        (MessageTable.to eq user.id) or (MessageTable.to eq -1)
                    }
                },true, cache = false
            )
        }.default {
            error("Not login")
        }
    }
    @Route(GET)
    @Api("标记已读")
    suspend fun read(call:RoutingCall) {
        call.checkParameters("id")
        val id = call.parameters["id"]!!.toLong()
        call.match {
            isLogin()
        }.then {
            val user = getUser().asEntity!!
            if (user.hasMessage(id)){
                Auth.dataManager.useDatabase()
                    .update(MessageTable){
                        set(MessageTable.read,true)
                        where { MessageTable.id eq id }
                    }
                call.ok("ok")
            }else{
                error("Not has message")
            }

        }.default {
            error("Not login")
        }
    }
}
fun UserDataEntity.hasMessage(id:Long):Boolean{
    return Auth.dataManager.useDatabase()
        .from(MessageTable)
        .select(MessageTable.id)
        .where {
            MessageTable.id eq id
        }.asIterable().toList().isNotEmpty()
}
fun MessageTable.sendMessage(
    from: Long = -1,
    to:List<Long> = emptyList(),
    title:String,
    subtitle:String,
    content: String
){
    to.forEach { user->
        Auth.dataManager.useDatabase()
            .insert(MessageTable){
                set(MessageTable.from,from)
                set(MessageTable.to,user)
                set(MessageTable.content,content)
                set(MessageTable.timestamp, System.currentTimeMillis())
                set(MessageTable.read,false)
                set(MessageTable.title,title)
                set(MessageTable.subtitle,subtitle)
            }
    }
}
fun UserDataEntity.sendMessage(from:Long = -1,title:String,
                               subtitle:String,content: String){
    MessageTable.sendMessage(from,arrayListOf(this.id),title,subtitle,content)
}