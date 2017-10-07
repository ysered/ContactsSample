@file:JvmName("ContentProviderUtils")

package com.ysered.contactssample.utils

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import com.ysered.contactssample.R


fun getPhoneTypeName(context: Context, type: Int): String? {
    // TODO: add rest of phone types
    val resourceId = when (type) {
        TYPE_MOBILE -> R.string.phone_type_mobile
        TYPE_WORK -> R.string.phone_type_work
        TYPE_HOME -> R.string.phone_type_home
        TYPE_OTHER -> R.string.phone_type_other
        else -> -1
    }
    return if (resourceId != -1) context.getString(resourceId) else null
}
