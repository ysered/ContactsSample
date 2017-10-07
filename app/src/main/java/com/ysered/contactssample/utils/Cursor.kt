@file:JvmName("CursorUtils")

package com.ysered.contactssample.utils

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.ysered.contactssample.data.Contact

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

fun Cursor.getUri(name: String): Uri? {
    val uriString = getString(name)
    return if (uriString.isNotBlank())
        Uri.parse(uriString)
    else
        null
}

/**
 * Reads data contacts content provider into [Contact] object.
 */
fun Cursor.getContact(): Contact {
    val id = getString(ContactsContract.Contacts._ID)
    val thumbnailUri = getUri(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
    val photoUri = getUri(ContactsContract.Contacts.PHOTO_URI)
    val displayName = getString(ContactsContract.Contacts.DISPLAY_NAME)
    return Contact(id, thumbnailUri, photoUri, displayName)
}