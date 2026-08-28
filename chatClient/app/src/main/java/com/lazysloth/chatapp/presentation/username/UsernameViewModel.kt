package com.lazysloth.chatapp.presentation.username

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysloth.chatapp.data.remote.MessageService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UsernameViewModel: ViewModel() {

    private val _usernameText = mutableStateOf("")
    val usernameText : State<String> = _usernameText

    private val _onJoinChat = MutableSharedFlow<String>()
    val onJoin = _onJoinChat.asSharedFlow()

    fun onUsernameChange(username: String) {
        _usernameText.value = username
    }

    fun onJoinClick() {
        viewModelScope.launch {
            if(usernameText.value.isNotBlank()) {
                _onJoinChat.emit(usernameText.value)
            }

        }
    }
}