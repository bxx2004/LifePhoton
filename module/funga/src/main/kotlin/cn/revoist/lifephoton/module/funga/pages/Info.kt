package cn.revoist.lifephoton.module.funga.pages

import cn.revoist.lifephoton.module.filemanagement.FileManagementAPI
import cn.revoist.lifephoton.module.funga.FungaPlugin
import cn.revoist.lifephoton.module.funga.data.table.DBInfoTable
import cn.revoist.lifephoton.plugin.data.maps
import cn.revoist.lifephoton.plugin.route.GET
import cn.revoist.lifephoton.plugin.route.Route
import cn.revoist.lifephoton.plugin.route.RouteContainer
import cn.revoist.lifephoton.plugin.route.checkParameters
import cn.revoist.lifephoton.plugin.route.ok
import io.ktor.server.routing.*

/**
 * @author 6hisea
 * @date  2025/4/13 13:42
 * @description: None
 */
@RouteContainer("funga","info")
object Info {
    @Route(GET)
    suspend fun getAllDatabase(call: RoutingCall){
        call.ok(FungaPlugin.dataManager.useDatabase()
            .maps(DBInfoTable,DBInfoTable.id))
    }
}