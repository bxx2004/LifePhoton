package cn.revoist.lifephoton.ktors

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.plugin.Plugin

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

private val plugins = ArrayList<Plugin>()
private lateinit var initServer:EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>

fun main(args: Array<String>) {
    val config = CommandLineConfig(args)
    val server = EmbeddedServer(config.rootConfig, Netty) {
        takeFrom(config.engineConfig)
        loadConfiguration(config.rootConfig.environment.config)
    }
    initServer = server
    server.application.configure()
    Booster.pluginLoad()
    printInfo()
    server.start(true)
}
private fun printInfo() {
    var info = "\n===== Arksha Database Info =====\n"
    info += "> meta\n"
    info += "  version: ${Booster.SYSTEM_VERSION},${Booster.VERSION}\n"
    info += "  author: Haixu Liu\n"
    info += "> plugins (count: ${plugins.size})\n"
    plugins.forEach {
        info += "  ${it.name}[${it.id}] author:${it.author} version:${it.version}\n"
    }
    info += "===== Arksha Database Info =====\n"
    initServer.application.log.info(info)
}
fun hasPlugin(id:String):Boolean{
    return plugins.any { it.id == id }
}

fun getPlugin(id:String):Plugin?{
    return plugins.find { it.id == id }
}

fun usePlugin(plugin: Plugin) {
    plugin.setApplication(initServer.application)
    plugin.logger = initServer.application.log
    plugins.add(plugin)
    plugin.configure()
    plugin.load()

}

fun Application.module() {

}



































private fun NettyApplicationEngine.Configuration.loadConfiguration(config: ApplicationConfig) {
    val deploymentConfig = config.config("ktor.deployment")
    loadCommonConfiguration(deploymentConfig)
    deploymentConfig.propertyOrNull("runningLimit")?.getString()?.toInt()?.let {
        runningLimit = it
    }
    deploymentConfig.propertyOrNull("shareWorkGroup")?.getString()?.toBoolean()?.let {
        shareWorkGroup = it
    }
    deploymentConfig.propertyOrNull("responseWriteTimeoutSeconds")?.getString()?.toInt()?.let {
        responseWriteTimeoutSeconds = it
    }
    deploymentConfig.propertyOrNull("requestReadTimeoutSeconds")?.getString()?.toInt()?.let {
        requestReadTimeoutSeconds = it
    }
    deploymentConfig.propertyOrNull("tcpKeepAlive")?.getString()?.toBoolean()?.let {
        tcpKeepAlive = it
    }
    deploymentConfig.propertyOrNull("maxInitialLineLength")?.getString()?.toInt()?.let {
        maxInitialLineLength = it
    }
    deploymentConfig.propertyOrNull("maxHeaderSize")?.getString()?.toInt()?.let {
        maxHeaderSize = it
    }
    deploymentConfig.propertyOrNull("maxChunkSize")?.getString()?.toInt()?.let {
        maxChunkSize = it
    }
}

