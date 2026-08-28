package com.lazysloth.chatapp

import android.app.Application
import com.lazysloth.chatapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ChatApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ChatApp)
            modules(appModule)
        }
    }
    }
