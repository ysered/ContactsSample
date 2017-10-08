package com.ysered.contactssample.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.ysered.contactssample.R
import com.ysered.contactssample.utils.getInt
import com.ysered.contactssample.utils.getString


data class Email(val kind: String, val address: String)

fun Cursor.getEmail(context: Context): Email? {
    val type = getInt(ContactsContract.CommonDataKinds.Email.TYPE)
    val kind = getEmailKind(context, type)
    return if (kind != null) {
        val email = getString(ContactsContract.CommonDataKinds.Email.ADDRESS)
        Email(kind, email)
    } else
        null
}

private fun getEmailKind(context: Context, type: Int): String? {
    // TODO: add rest of types
    val resourceId = when (type) {
        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> R.string.item_type_mobile
        ContactsContract.CommonDataKinds.Email.TYPE_WORK -> R.string.item_type_work
        ContactsContract.CommonDataKinds.Email.TYPE_HOME -> R.string.item_type_home
        ContactsContract.CommonDataKinds.Email.TYPE_OTHER -> R.string.item_type_other
        else -> -1
    }
    return if (resourceId != -1) context.getString(resourceId) else null
}
