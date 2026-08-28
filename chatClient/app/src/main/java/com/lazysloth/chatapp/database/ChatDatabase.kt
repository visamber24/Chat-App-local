package com.lazysloth.chatapp.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

import com.lazysloth.chatapp.data.dao.MessageDao
import com.lazysloth.chatapp.domain.model.MessageDb


@Database(entities = [MessageDb:: class], version = 1)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getChatDatabase(context: Context): ChatDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    name = "chat_database",
                    context = context,
                    klass = ChatDatabase::class.java
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }

        }
    }
}