package com.testziliao.app

import android.app.Application
import com.testziliao.app.data.di.AppContainer

class TestAppApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
