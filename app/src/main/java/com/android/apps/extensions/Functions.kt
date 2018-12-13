package com.android.apps.extensions

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import java.lang.Exception

fun Int.getString(context: Context, vararg args: Any) = try {
    context.getString(this, args)
} catch (e: Exception) {
    ""
}

fun Int.getDrawable(context: Context) = try {
    ContextCompat.getDrawable(context, this)
} catch (e: Exception) {
    null
}