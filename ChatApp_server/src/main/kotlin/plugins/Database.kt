package com.example.plugins
import com.example.plugins.configureDatabases
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped

fun Application.configureDatabases() : MongoDatabase {
    val connectionString = System.getenv("MONGODB_URI") ?: "mongodb://localhost:27017"
    val mongoClient = MongoClient.create(connectionString)
    val database = mongoClient.getDatabase("ktor_store")

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return database
}