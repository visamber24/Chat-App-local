package com.lazysloth.chatapp.data.remote

import android.os.Build
import com.lazysloth.chatapp.domain.model.Message

interface MessageService {
    suspend fun getAllMessages(): List<Message>

    companion object {
        private val HOST_IP = if (Build.FINGERPRINT.startsWith("generic") ||
            Build.MODEL.contains("google_sdk") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu")
        ) {
            "10.0.2.2" // Emulator route to your computer
        } else {
            "172.31.77.194"
        }

        val BASE_URL = "http://$HOST_IP:5005"
    }

    sealed class Endpoints(val url: String) {
        object getAllMessages : Endpoints("$BASE_URL/messages")
    }
}

