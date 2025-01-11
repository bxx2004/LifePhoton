package cn.revoist.lifephoton.ktors

import cn.revoist.lifephoton.plugin.event.events.AuthenticationEvent
import cn.revoist.lifephoton.plugin.route.ErrorResponse
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.css.*
import kotlinx.serialization.Serializable

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
@Serializable
class UserSession(var accessToken:String,var refreshToken:String)
fun Application.configure() {
    install(Sessions) {
        cookie<UserSession>("user") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 1000 * 30
        }
    }
    install(Authentication){
        session<UserSession>("auth-session"){
            validate { session ->
                val event = AuthenticationEvent(session?:UserSession("-1","-1"),false).call() as AuthenticationEvent
                this.response.call.sessions.set(event.user)
                if (event.truth){
                    event.user
                }else{
                    this.response.call.respond(
                        ErrorResponse(false,"Please login")
                    )
                    null
                }
            }
            challenge {
                call.respond(
                    ErrorResponse(false,"Please login")
                )
            }
        }
    }
    install(ContentNegotiation){
        gson {  }
        /*
        json(Json {
            prettyPrint = true
            isLenient=true
        })
         */
    }
    configureHTTP()
    configureRouting()
}