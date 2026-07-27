package com.lazysloth.chatapp.presentation.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysloth.chatapp.data.remote.ChatSocketService
import com.lazysloth.chatapp.data.remote.MessageService
import com.lazysloth.chatapp.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {

        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                val result = chatSocketService.initSession(username)
                Log.d("Socket", "socket no. $chatSocketService")
                when (result) {
                    is Resource.Success -> {
                        chatSocketService.observeMessages()
                            .collect { newMessage ->
                                _state.update {state ->
                                    state.copy(
                                        message = _state.value.message + newMessage,
                                        isLoading = false
                                    )
                                }
                                getAllMessages()
                                Log.d("Message","formatted time : ${newMessage.formattedTime}")
                                _messageText.value = ""
                            }
//                            .stateIn(
//                                scope = TODO(),
//                                started = TODO(),
//                                initialValue = TODO()
//                            )
                    }

                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")

                    }
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = messageService.getAllMessages()
            _state.update {
                it.copy(
                    message = result,
                    isLoading = false
                )
            }
        }
    }
    fun sendMessage() {
        viewModelScope.launch {
            if (_messageText.value.isNotBlank()) {
                chatSocketService.sendMessage(messageText.value)
                getAllMessages()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}