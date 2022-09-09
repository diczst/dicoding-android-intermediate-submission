package com.neonusa.submission1

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.neonusa.submission1.core.di.appModule
import com.neonusa.submission1.core.di.repositoryModule
import com.neonusa.submission1.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
        startKoin{
            androidContext(this@MyApp)
            modules(listOf(appModule, repositoryModule, viewModelModule))
        }
    }
}