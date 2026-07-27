package com.example

import com.example.data.MessageDataSource
import com.example.data.MessageDataSourceImpl
import com.example.data.room.RoomController
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.koin.dsl.module

fun mainModule(database: MongoDatabase) = module {

    single<MongoDatabase> { database }
    single<MessageDataSource> {
        MessageDataSourceImpl(get())
    }
    single<RoomController> { RoomController(get()) }
}