package com.example.data

import com.example.data.model.Message
import com.mongodb.client.model.Sorts
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class MessageDataSourceImpl(private val db: MongoDatabase): MessageDataSource {

    val messages = db.getCollection<Message>("message")
    override suspend fun getAllMessages(): List<Message> {
        return messages
            .find()
            .sort(Sorts.descending("timestamp"))
            .toList()
    }

    override suspend fun insetMessage(message: Message) {
        messages.insertOne(message)
    }
}