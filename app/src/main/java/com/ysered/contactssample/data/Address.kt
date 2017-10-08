package com.ysered.contactssample.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.ysered.contactssample.R
import com.ysered.contactssample.utils.getInt
import com.ysered.contactssample.utils.getString


data class Address(val kind: String, val fullAddress: String)

fun Cursor.getAddress(context: Context): Address? {
    val type = getInt(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)
    val kind = getAddressKind(context, type)
    return if (kind != null) {
        val street = getString(ContactsContract.CommonDataKinds.StructuredPostal.STREET)
        val city = getString(ContactsContract.CommonDataKinds.StructuredPostal.CITY)
        val country = getString(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)
        Address(kind, "$street $city $country")
    } else
        null
}

private fun getAddressKind(context: Context, type: Int): String? {
    // TODO: add rest of types
    val resourceId = when (type) {
        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK -> R.string.item_type_work
        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME -> R.string.item_type_home
        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER -> R.string.item_type_other
        else -> -1
    }
    return if (resourceId != -1) context.getString(resourceId) else null
}
