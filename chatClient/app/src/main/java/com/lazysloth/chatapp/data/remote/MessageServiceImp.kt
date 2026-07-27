package com.lazysloth.chatapp.data.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.lazysloth.chatapp.data.dto.MessageDto
import com.lazysloth.chatapp.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MessageServiceImp(
   private val client: HttpClient
): MessageService {
   @RequiresApi(Build.VERSION_CODES.O)
   override suspend fun getAllMessages(): List<Message> {
      return try {
         client.get(MessageService.Endpoints.getAllMessages.url).body<List<MessageDto>>()
            .map { it.toMessage() }

      } catch (e: Exception) {
         emptyList()
      }
   }
}