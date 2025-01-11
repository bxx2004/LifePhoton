package cn.revoist.lifephoton.extensions.auth

import cn.revoist.lifephoton.Booster
import cn.revoist.lifephoton.extensions.auth.data.Tools
import cn.revoist.lifephoton.plugin.Plugin
import cn.revoist.lifephoton.plugin.anno.AutoUse
import cn.revoist.lifephoton.plugin.anno.Pages
import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import cn.revoist.lifephoton.plugin.event.registerListener
import io.ktor.http.*
import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.delivery.mailerBuilder
import java.io.File

/**
 * @author 6hisea
 * @date  2025/1/7 19:54
 * @description: Auth
 */
@AutoUse(Booster.SystemVersion.ADVANCED)
object Auth :Plugin(){
    override val name: String
        get() = "Auth"
    override val author: String
        get() = "Haixu Liu"
    override val version: String
        get() = "1"

    override fun load() {
        val mailer = mailerBuilder("smtp.qq.com",587,"no-replay-revoist@qq.com","nfxiwxpavjtvdjdh")
        MailerManager.defaultMailer = mailer
        registerListener(AuthenticationEvent::class.java){
            if (Tools.checkToken(it.user)){
                it.truth = true
            }
        }
    }

    override fun configure() {
        optional("multi-email-user",false)
    }
}