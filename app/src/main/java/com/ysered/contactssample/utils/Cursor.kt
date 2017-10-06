@file:JvmName("CursorUtils")

package com.ysered.contactssample.utils

import android.database.Cursor


fun Cursor.forEach(body: Cursor.(cursor: Cursor) -> Unit) {
    if (!isClosed && count > 0 && moveToFirst()) {
        while (!isAfterLast) {
            body(this)
            moveToNext()
        }
        close()
    }
}

fun Cursor.getString(name: String): String = getString(getColumnIndex(name))
