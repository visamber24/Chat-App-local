package com.lazysloth.chatapp.domain.repository

import com.lazysloth.chatapp.domain.model.MessageDb
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun insert(messageDb: List<MessageDb>)

    suspend fun delete(messageDb: MessageDb)

    suspend fun update(messageDb: MessageDb)

    fun getAllMessage(): Flow<List<MessageDb>>
    suspend fun getLastMessage(): String?
}