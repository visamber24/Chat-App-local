package com.lazysloth.chatapp.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.lazysloth.chatapp.domain.model.MessageDb
import com.lazysloth.chatapp.domain.model.MessageUi
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class MessageDto(
    val text:String,
    val timestamp: Long,
    val username: String,
    val id: String,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toMessageUi(): MessageUi {
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM")
        val formattedDate = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
        return MessageUi(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }
    fun toMessageDb()=  MessageDb(
        messageId = id,
        text = text,
        timestamp = timestamp,
        username = username
    )

}
