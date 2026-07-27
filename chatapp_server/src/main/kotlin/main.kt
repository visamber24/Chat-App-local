package com.example


import com.example.plugins.configureDatabases
import com.example.plugins.configureMonitoring
import com.example.plugins.configureSerialization
import com.example.plugins.configureWebSockets
import io.ktor.server.application.Application
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
fun Application.module() {

    configureWebSockets()
    configureRouting()
    val database = configureDatabases()
    startKoin {
        modules(mainModule(database))
    }
    configureSerialization()
    configureMonitoring()
    configureSecurity()


}