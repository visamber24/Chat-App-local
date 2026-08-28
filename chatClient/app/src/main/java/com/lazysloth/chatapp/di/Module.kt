package com.lazysloth.chatapp.di

import com.lazysloth.chatapp.data.ChatSocketServiceImpl
import com.lazysloth.chatapp.data.dao.MessageDao
import com.lazysloth.chatapp.data.remote.ChatSocketService
import com.lazysloth.chatapp.data.remote.MessageService
import com.lazysloth.chatapp.data.remote.MessageServiceImp
import com.lazysloth.chatapp.data.repository.MessageRepositoryImpl
import com.lazysloth.chatapp.database.ChatDatabase
import com.lazysloth.chatapp.domain.repository.MessageRepository
import com.lazysloth.chatapp.presentation.chat.ChatViewModel
import com.lazysloth.chatapp.presentation.username.UsernameViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // room database
    single<ChatDatabase> { ChatDatabase.getChatDatabase(get()) }
    // Dao
    single<MessageDao> { get<ChatDatabase>().messageDao() }

    single<MessageRepository> {
        MessageRepositoryImpl(get())
    }

    single {
        HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(ContentNegotiation) {
                json(Json {isLenient = true; ignoreUnknownKeys = true})
            }
        }
    }
    single <MessageService> { MessageServiceImp(get(),get()) }

    single<ChatSocketService> { ChatSocketServiceImpl(get()) }

    viewModel {
        UsernameViewModel()
    }
    viewModel {
        ChatViewModel(get(),get(),get(), get())
    }
}