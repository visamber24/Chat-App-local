package com.example

import com.example.routes.chatSocket
import com.example.routes.getAllMessages
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }
    routing {
        chatSocket()
        getAllMessages()
        staticResources("/content", "myContent")
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/test1"){
            val text = "<h1>Hellow from vishu</h1>"
            val type = ContentType.Text.Html
            call.respondText(text,type)
        }
        get("error-test"){
            throw IllegalStateException("Too Busy")
        }
    }
}