package com.lazysloth.chatapp.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.lazysloth.chatapp.domain.model.Message
import kotlinx.serialization.Serializable
import java.sql.Time
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Serializable
data class MessageDto(
    val text:String,
    val timestamp: Long,
    val username: String,
    val id: String,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toMessage(): Message {
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM")
        val formattedDate = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
        return Message(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }
}
