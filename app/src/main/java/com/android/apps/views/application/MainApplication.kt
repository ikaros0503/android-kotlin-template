package com.android.apps.views.application

import android.app.Application
import com.android.apps.utils.prefs.PreferencesUtils

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO init utils
        initializeHelper()
    }

    private fun initializeHelper() {
        with(applicationContext) {
            PreferencesUtils.init(this)
        }
    }

}