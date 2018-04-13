package com.android.apps.utils

import android.R
import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast

/**
 * Common utils, contains all global function support
 * for developer
 */

fun showToast(context: Context, textResId: Int) {
    Toast.makeText(context, textResId, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun confirm(context: Context, title: String = "", message: String = ""): AlertDialog {
    return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.yes, { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            .setNegativeButton(R.string.no, { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            .create()
}