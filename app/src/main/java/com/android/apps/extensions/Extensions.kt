package com.android.apps.extensions

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast
import org.json.JSONArray

/**
 * JSONArray
 */
fun <R> JSONArray.map(transform: (Any) -> R): MutableList<R> {
    return (0 until length()).asSequence().map { transform(get(it)) }.toMutableList()
}

fun JSONArray.forEach(body: (Any) -> Unit) {
    (0 until length()).asSequence().map<Int, Any> { get(it) }.forEach(body)
}

/**
 *  AlertDialog
 */

fun AlertDialog.onConfirmed(action: () -> Unit): AlertDialog {
    this.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.yes), { _, _ -> action.invoke() })
    return this
}

fun AlertDialog.onDenied(action: () -> Unit): AlertDialog {
    this.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.no), { _, _ -> action.invoke() })
    return this
}

/**
 * Context
 */

fun Context.toast(message: String = "", messageId: Int = -1) {
    when {
        message.isNotBlank() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        messageId != -1 -> Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
        else -> throw IllegalArgumentException("You have not set any message.")
    }
}