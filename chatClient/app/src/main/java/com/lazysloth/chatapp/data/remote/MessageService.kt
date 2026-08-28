package com.lazysloth.chatapp.data.remote

import com.lazysloth.chatapp.domain.model.MessageUi

interface MessageService {
    suspend fun getAllMessages(): List<MessageUi>
    suspend fun saveMessagesToDatabase()
    companion object {
//        private val HOST_IP = if (Build.FINGERPRINT.startsWith("generic") ||
//            Build.MODEL.contains("google_sdk") ||
//            Build.HARDWARE.contains("goldfish") ||
//            Build.HARDWARE.contains("ranchu")
//        ) {
//            "10.0.2.2" // Emulator route to your computer
//        } else {
//            "chat-app-local.onrender.com"
//        }

        val BASE_URL = "http://chat-app-local.onrender.com"
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessages : Endpoints("$BASE_URL/messages")
    }
}

