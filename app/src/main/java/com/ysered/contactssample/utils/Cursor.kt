@file:JvmName("CursorUtils")

package com.ysered.contactssample.utils

import android.database.Cursor
import android.net.Uri


fun Cursor.first(body: Cursor.(cursor: Cursor) -> Unit) {
    if (!isClosed && count > 0 && moveToFirst())
        body(this)
}

/**
 * Iterates through [Cursor] and executes [body] in its context.
 */
fun Cursor.forEach(body: Cursor.(cursor: Cursor) -> Unit) {
    if (!isClosed && count > 0 && moveToFirst()) {
        while (!isAfterLast) {
            body(this)
            moveToNext()
        }
    }
}

fun Cursor.getInt(name: String): Int = getInt(getColumnIndex(name))

fun Cursor.getString(name: String): String = getString(getColumnIndex(name)) ?: ""

fun Cursor.getBoolean(name: String): Boolean = getInt(name) == 1

fun Cursor.getUri(name: String): Uri? {
    val uriString = getString(name)
    return if (uriString.isNotBlank())
        Uri.parse(uriString)
    else
        null
}
