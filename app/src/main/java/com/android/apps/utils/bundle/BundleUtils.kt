package com.android.apps.utils.bundle

import android.os.Bundle

fun Bundle.init(vararg data: Pair<String, Any>): Bundle {
    return this.apply {
        for ((key, value) in data) {
            when (value) {
                is Byte -> putByte(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Array<*> -> {
                    if (value.all { it is String }) {
                        @Suppress("UNCHECKED_CAST")
                        putStringArray(key, value as Array<String>)
                    }
                }
            }
        }
    }
}

fun <T> Bundle.get(key: String, default: T): T {
    @Suppress("UNCHECKED_CAST")
    return get(key) as? T ?: default
}
