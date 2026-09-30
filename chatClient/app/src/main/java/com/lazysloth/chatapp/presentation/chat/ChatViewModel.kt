package com.lazysloth.chatapp.presentation.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysloth.chatapp.data.remote.ChatSocketService
import com.lazysloth.chatapp.data.remote.MessageService
import com.lazysloth.chatapp.domain.model.MessageUi
import com.lazysloth.chatapp.domain.repository.MessageRepository
import com.lazysloth.chatapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChatViewModel(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    val containsGif by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToChat() {
        getAllMessages()

        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                messageService.getAllMessages()
                messageService.saveMessagesToDatabase()
                val result = chatSocketService.initSession(username)
                Log.d("Socket", "socket no. $chatSocketService" + "and result : $result")
                when (result) {
                    is Resource.Success -> {
                        chatSocketService.observeMessages()
                            .onStart {
                                Log.d("Socket", "observeMessages collection STARTED")
                            }
                            .onEach { message ->
                                Log.d("Socket", "Flow emitted: $message")
                            }
                            .catch { e ->
                                Log.e("Socket", "observeMessages ERROR", e)
                            }
                            .onCompletion { cause ->
                                Log.d("Socket", "observeMessages completed: $cause")
                            }
                            .collect { newMessage ->
                                Log.d("After Success", "$newMessage")
                                _state.update { state ->
                                    Log.d("ChatViewModel","${listOf(newMessage)}" )
                                    state.copy(
                                        messageUi = listOf(newMessage) + _state.value.messageUi,
                                        isLoading = false
                                    )
                                }
                                Log.d("Message", "formatted time : ${newMessage.formattedTime}")
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

    // Get messages from database
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllMessages() {
        Log.d("Messages", "getAllMessages called")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            messageRepository.getAllMessage()
                .map { list ->
                    list.map { message ->
                        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM")
                        val formattedDate =
                            Instant.ofEpochMilli(message.timestamp)
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)
                        MessageUi(
                            text = message.text,
                            username = message.username,
                            formattedTime = formattedDate
                        )
                    }

                }.flowOn(Dispatchers.Default)
                .collect { messages ->
                    Log.d(
                        "From LocalDB",
                        "Received ${messages.size}: $messages"
                    )
                    _state.update {
                        it.copy(
                            messageUi = messages,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun sendMessage() {
        if (containsGif) {
        }
        viewModelScope.launch {
            Log.d("Send Message", "send button clicked")
            if (_messageText.value.isNotBlank()) {
                chatSocketService.sendMessage(messageText.value, gif = Byte.MAX_VALUE)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}