package com.lazysloth.chatapp.data.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.lazysloth.chatapp.data.dao.MessageDao
import com.lazysloth.chatapp.data.dto.MessageDto
import com.lazysloth.chatapp.domain.model.MessageUi
import com.lazysloth.chatapp.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MessageServiceImp(
    private val client: HttpClient,
    private val messageRepository: MessageRepository,
) : MessageService {
    @RequiresApi(Build.VERSION_CODES.O)
    var latestMessages: List<MessageDto> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllMessages(): List<MessageUi> {
        return try {
            latestMessages =
                client.get(MessageService.Endpoints.GetAllMessages.url).body<List<MessageDto>>()
            if (latestMessages.isNotEmpty()) {
                latestMessages.forEach {
                    Log.d("Message Received", it.id)
                }
            }


            latestMessages.map { it.toMessageUi() }

        } catch (e: Exception) {
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveMessagesToDatabase() {
        Log.d("DB", "1. saveMessagesToDatabase called")
        val dbMsg = messageRepository.getLastMessage() ?: ""
        if (latestMessages.last().id != dbMsg) {
            Log.d("DB", "2. before repository insert")
            messageRepository.insert(latestMessages.map { it.toMessageDb() })
            Log.d("DB", "4. DAO insert finished")
        }
    }
}