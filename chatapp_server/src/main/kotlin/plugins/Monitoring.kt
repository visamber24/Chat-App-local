package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import java.util.logging.Level


fun Application.configureMonitoring() {
    install(CallLogging) {
         Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}