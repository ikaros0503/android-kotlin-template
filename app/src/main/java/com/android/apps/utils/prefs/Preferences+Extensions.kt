package com.android.apps.utils.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Private instance
 */
private var prefsDefault: SharedPreferences? = null

object Preferences {
    private const val PREFS_NAME_DEFAULT = "default"

    /**
     * Visibility instance
     */
    val default: SharedPreferences
        get() = prefsDefault ?: throw IllegalAccessError("")

    fun inject(context: Context) {
        prefsDefault = context.prefs(PREFS_NAME_DEFAULT)
    }
}

/**
 * Get SharePreferences with [name]
 */
fun Context.prefs(name: String): SharedPreferences {
    return getSharedPreferences(name, MODE_PRIVATE)
}

/**
 *  SharedPreferences extensions
 */

/**
 * Put the [value] to SharedPreferences with [key]
 */
fun SharedPreferences.put(key: String, value: Any) {
    with(edit()) {
        when (value) {
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Set<*> -> {
                if (value.all { it is String }) {
                    @Suppress("UNCHECKED_CAST")
                    putStringSet(key, value as Set<String>)
                }
            }
        }
        apply()
    }
}

/**
 * Get the value of [key] from SharedPreferences.
 * If key not found, then return [default]
 */
@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.get(key: String, default: T): T {
    return all[key] as? T ?: default
}

/**
 * Remove the [key] from SharedPreferences
 */
fun SharedPreferences.remove(key: String) {
    edit().remove(key).apply()
}

