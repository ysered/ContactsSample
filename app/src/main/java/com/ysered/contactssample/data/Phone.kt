package com.ysered.contactssample.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.ysered.contactssample.R
import com.ysered.contactssample.utils.getInt
import com.ysered.contactssample.utils.getString

data class Phone(val kind: String, val number: String)

fun Cursor.getPhone(context: Context): Phone? {
    val type = getInt(ContactsContract.CommonDataKinds.Phone.TYPE)
    val kind = getPhoneKind(context, type)
    return if (kind != null) {
        val number = getString(ContactsContract.CommonDataKinds.Phone.NUMBER)
        Phone(kind, number)
    } else
        null
}

private fun getPhoneKind(context: Context, type: Int): String? {
    // TODO: add rest of phone types
    val resourceId = when (type) {
        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> R.string.item_type_mobile
        ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> R.string.item_type_work
        ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> R.string.item_type_home
        ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> R.string.item_type_other
        else -> -1
    }
    return if (resourceId != -1) context.getString(resourceId) else null
}
