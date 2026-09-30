package com.lazysloth.chatapp.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.lazysloth.chatapp.domain.model.MessageDb
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(messageDb: List<MessageDb>)

    @Delete
    suspend fun deleteMessage(messageDb: MessageDb)

    @Update
    suspend fun updateMessage(messageDb: MessageDb)

    @Query("SELECT * FROM message ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageDb>>
    @Query("SELECT messageId FROM message ORDER BY timestamp DESC LIMIT 1  ")
    suspend fun getLastMessage(): String?
}