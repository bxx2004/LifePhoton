package cn.revoist.lifephoton.plugin.event.events

import cn.revoist.lifephoton.ktors.UserSession
import cn.revoist.lifephoton.plugin.event.Event


class AuthenticationEvent(val user:UserSession,var truth:Boolean) : Event() {
}