package com.example.data.room

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        userName: String,
        sessionId: String,
        socket: WebSocketSession,
    ){
        if(members.containsKey(userName)){
            throw MemberAlreadyExistsException()
        }
        members[userName] = Member(
            userName = userName,
            sessionId = sessionId,
            socket =  socket
        )
    }
    suspend fun sendMessage(senderUsername: String, message: String){
        val messageEntity = Message(
            text = message,
            username = senderUsername,
            timestamp = System.currentTimeMillis()
        )
        messageDataSource.insetMessage(messageEntity)
        val parsedMessage = Json.encodeToString(messageEntity)

        members.values.forEach { member ->
            member.socket.send(Frame.Text(parsedMessage))
        }
    }
    suspend fun getAllMessages(): List<Message>{
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if(members.containsKey(username)){
            members.remove(username)
        }
    }
}