package com.android.apps.views.application

import android.app.Application
import com.android.apps.utils.prefs.Preferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO init utils
        initializeHelper()
    }

    private fun initializeHelper() {
        with(applicationContext) {
            Preferences.inject(this)
        }
    }
}