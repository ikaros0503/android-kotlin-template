package com.android.apps.repository.uiconfig

import android.content.Context
import android.util.Log
import com.android.apps.BuildConfig
import com.android.apps.extensions.readAssets
import com.android.apps.utils.prefs.Preferences
import com.android.apps.utils.prefs.get
import com.android.apps.utils.prefs.put
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class UIConfigRepository {

    var config: Config = Config(
        Config.WebView("", "")
    )
        private set


    fun fetchConfig(context: Context, isNetworkAvailable: Boolean): Flowable<Config> {
        return Flowable.fromCallable<Config> {
            if (isNetworkAvailable) {
                URL.httpGet().timeout(DEFAULT_TIMEOUT).responseObject<Config>().component3().fold({
                    Log.w("UIConfig", "Retrieved")
                    it.also {
                        config = it
                        Preferences.default.put(PREFS_KEY_CONFIG_DATA, Gson().toJson(it))
                    }
                }) { error ->
                    error.printStackTrace()
                    Log.w("UIConfig", "Failed to retrieved")
                    readConfigStringFromCachedOrAssets(context).also {
                        config = it
                    }
                }.also {

                }
            } else {
                config = readConfigStringFromCachedOrAssets(context)
            }
            config
        }.subscribeOn(Schedulers.io()).onBackpressureLatest()
    }

    private fun readConfigStringFromCachedOrAssets(context: Context): Config {
        val configString =
            Preferences.default.get(PREFS_KEY_CONFIG_DATA, "").takeIf { it.isNotEmpty() }
                ?: readConfigFromAssets(context)
        return Gson().fromJson(configString, Config::class.java)
    }

    private fun readConfigFromAssets(context: Context): String {
        return context.readAssets("uiconfig.json")
    }

    data class Config(
        val webview: WebView
    ) {

        data class WebView(
            val url: String,
            val script: String
        )
    }


    companion object {
        private const val URL =
            "http://api.musicplus.io/data/${BuildConfig.APPLICATION_ID}.json"
        private const val PREFS_PREFIX = "com.android.apps.utils.default"
        private const val PREFS_KEY_CONFIG_DATA = "$PREFS_PREFIX.UIConfig.data"
        private const val KEY_DEFAULT = "default"


        private const val DEFAULT_TIMEOUT = 7000
        private var INSTANCE: UIConfigRepository? = null


        val instance: UIConfigRepository
            get() = INSTANCE ?: synchronized(UIConfigRepository::class.java) {
                INSTANCE ?: UIConfigRepository().also {
                    INSTANCE = it
                }
            }
    }
}