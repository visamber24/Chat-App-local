package com.lazysloth.chatapp.domain.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "message")
data class MessageDb(
    @PrimaryKey
    val messageId: String =  "",
    val username: String = "",
    val text: String = "",
//    val senderId: Int,
//    val receiverId: Int,
    val timestamp: Long,

    )