package com.lazysloth.chatapp.data.remote

import com.lazysloth.chatapp.domain.model.Message
import com.lazysloth.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()
    companion object {
        const val BASE_URL = "ws://192.168.1.14:5005"
    }
    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}