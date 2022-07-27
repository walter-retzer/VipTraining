package com.wdretzer.viptraining.util

import android.app.Application
import android.content.Context

class AppUtil: Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }

    companion object{
        var appContext: Context? = null
    }
}
