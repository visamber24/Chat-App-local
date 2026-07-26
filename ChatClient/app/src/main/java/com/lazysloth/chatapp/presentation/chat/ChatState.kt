package com.lazysloth.chatapp.presentation.chat

import com.lazysloth.chatapp.domain.model.Message

data class ChatState(
    val message: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
