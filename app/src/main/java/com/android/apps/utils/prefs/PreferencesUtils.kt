package com.android.apps.utils.prefs

import android.content.Context
import android.content.SharedPreferences
import java.util.*

object PreferencesUtils {

    private var mPrefs: SharedPreferences? = null
    private const val SHARED_PREFS_NAME = "com.android.apps.utils.prefs.SharedPrefs"

    fun init(context: Context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun put(key: String, value: Any) {
        mPrefs?.edit()?.apply {
            when (value) {
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
            }
            apply()
        }
    }


    /**
     * Remove preferences by key
     * @param key String
     */
    fun remove(key: String) {
        mPrefs?.edit()?.apply {
            remove(key)
            apply()
        }
    }

    fun contains(key: String): Boolean {
        return mPrefs?.contains(key) ?: false
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> retrieve(key: String, defaultValue: T? = null): T? {
        return mPrefs?.all?.get(key) as? T ?: defaultValue
    }
}