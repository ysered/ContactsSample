package com.ysered.contactssample.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.ysered.contactssample.R
import com.ysered.contactssample.utils.getInt
import com.ysered.contactssample.utils.getString

data class Phone(val type: String, val number: String)

fun Cursor.getPhone(context: Context): Phone? {
    val type = getInt(ContactsContract.CommonDataKinds.Phone.TYPE)
    val typeName = getPhoneTypeName(context, type)
    return if (typeName != null) {
        val number = getString(ContactsContract.CommonDataKinds.Phone.NUMBER)
        Phone(typeName, number)
    } else
        null
}

private fun getPhoneTypeName(context: Context, type: Int): String? {
    // TODO: add rest of phone types
    val resourceId = when (type) {
        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> R.string.phone_type_mobile
        ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> R.string.phone_type_work
        ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> R.string.phone_type_home
        ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> R.string.phone_type_other
        else -> -1
    }
    return if (resourceId != -1) context.getString(resourceId) else null
}
