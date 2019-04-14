package com.android.apps.extensions

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.json.JSONArray
import java.lang.Exception

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
    this.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.yes)) { _, _ -> action.invoke() }
    return this
}

fun AlertDialog.onDenied(action: () -> Unit): AlertDialog {
    this.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.no)) { _, _ -> action.invoke() }
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

fun Context.goStore(packageId: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageId")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}



/**
 * Int
 */
val Int.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

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

/**
 * Float
 */
val Float.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)