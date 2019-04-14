package com.android.apps.utils

import android.content.Context
import android.os.StrictMode
import androidx.appcompat.app.AlertDialog
import java.net.InetAddress

/**
 * Common utils, contains all global function support
 * for developer
 */

fun confirm(context: Context, title: String = "", message: String = ""): AlertDialog {
    return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
}

fun networkAvailable(): Boolean {
    return try {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ipAddr = InetAddress.getByName("google.com") //You can replace it with your name
        ipAddr.toString() != ""
    } catch (e: Exception) {
        false
    }
}