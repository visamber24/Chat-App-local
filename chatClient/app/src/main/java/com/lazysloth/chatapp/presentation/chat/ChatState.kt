package com.lazysloth.chatapp.presentation.chat

import com.lazysloth.chatapp.domain.model.MessageUi
import com.lazysloth.chatapp.domain.model.MessageType

data class ChatState(
    val messageType : MessageType = MessageType.Text,
    val messageUi: List<MessageUi> = emptyList(),
    val isLoading: Boolean = false
)
