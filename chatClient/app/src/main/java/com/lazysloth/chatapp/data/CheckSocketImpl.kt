package com.lazysloth.chatapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.lazysloth.chatapp.data.dto.MessageDto
import com.lazysloth.chatapp.data.remote.ChatSocketService
import com.lazysloth.chatapp.domain.model.Message
import com.lazysloth.chatapp.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient
): ChatSocketService {
    private var socket: WebSocketSession? = null
    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocket.url}?username=$username")
            }
            if (socket?.isActive == true ) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Couldn't establish the connection")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text) ?.readText() ?: ""
                    val messageDto = Json.decodeFromString(MessageDto.serializer(),json)
                    messageDto.toMessage()
                }?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }


    override suspend fun closeSession() {
        socket?.close()
    }
}