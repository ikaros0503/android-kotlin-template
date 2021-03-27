package com.android.apps.views.application

import android.app.Application
import com.android.apps.BuildConfig
import com.android.apps.utils.prefs.Preferences
import timber.log.Timber

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // TODO init utils
        initializeHelper()
    }

    private fun initializeHelper() {
        with(applicationContext) {
            Preferences.inject(this)
        }
    }
}