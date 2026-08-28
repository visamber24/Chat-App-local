package com.lazysloth.chatapp.data.remote

import com.lazysloth.chatapp.domain.model.MessageUi
import com.lazysloth.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String, gif: Byte)

    fun observeMessages(): Flow<MessageUi>

    suspend fun closeSession()
    companion object {
        const val BASE_URL = "ws://chat-app-local.onrender.com"
    }
    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}