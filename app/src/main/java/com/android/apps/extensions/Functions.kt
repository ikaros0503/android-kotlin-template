package com.android.apps.extensions

import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.lang.Exception

/**
 * Check if device android versions is Android 5 above
 */
fun isLollipopAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

/**
 * Check if device android versions is Android 6 above
 */
fun isMarshmallowAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

/**
 * Check if device android versions is Android O above
 */
fun isOreoAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O