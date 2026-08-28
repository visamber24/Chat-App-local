package com.lazysloth.chatapp.data.repository

import android.util.Log
import com.lazysloth.chatapp.data.dao.MessageDao
import com.lazysloth.chatapp.domain.model.MessageDb
import com.lazysloth.chatapp.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(val messageDao: MessageDao): MessageRepository {
    override suspend fun insert(messageDb: List<MessageDb>) {
        Log.d("DB", "3. repository insert called")
        messageDao.insertMessage(messageDb)
    }

    override suspend fun delete(messageDb: MessageDb) {
        messageDao.deleteMessage(messageDb)
    }

    override suspend fun update(messageDb: MessageDb) {
        messageDao.updateMessage(messageDb)
    }

    override fun getAllMessage(): Flow<List<MessageDb>> {
        return messageDao.getAllMessages()
    }

    override suspend fun getLastMessage(): String? {
        return messageDao.getLastMessage()
    }
}