package com.ysered.contactssample.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.ysered.contactssample.data.*


object ContentProviderUtils {

    fun readContact(context: Context, contactId: String): Contact? = context.queryContent(
            uri = ContactsContract.Contacts.CONTENT_URI,
            projection = null,
            selection = "${ContactsContract.Contacts._ID} = ?",
            selectionArgs = arrayOf(contactId),
            sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY,
            cursorReader = { cursor ->
                cursor.moveToFirst()
                cursor.getContact()
            }
    )

    fun readContactList(context: Context): List<Contact> = context.queryContent(
            uri = ContactsContract.Contacts.CONTENT_URI,
            projection = null,
            selection = null,
            selectionArgs = null,
            sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY,
            cursorReader = { cursor ->
                cursor.map { getContact() }
            }
    ) ?: emptyList()

    fun readPhoneList(context: Context, contactId: String): List<Phone> = context.queryContent(
            uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection = null,
            selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            selectionArgs = arrayOf(contactId),
            sortOrder = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY,
            cursorReader = { cursor -> cursor.map { getPhone(context)!! } }
    ) ?: emptyList()

    fun readEmailList(context: Context, contactId: String): List<Email> = context.queryContent(
            uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            projection = null,
            selection = "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            selectionArgs = arrayOf(contactId),
            sortOrder = ContactsContract.CommonDataKinds.Email.SORT_KEY_PRIMARY,
            cursorReader = { cursor -> cursor.map { getEmail(context)!! } }
    ) ?: emptyList()

    fun readAddressList(context: Context, contactId: String): List<Address> = context.queryContent(
            uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            projection = null,
            selection = "${ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID} = ?",
            selectionArgs = arrayOf(contactId),
            sortOrder = ContactsContract.CommonDataKinds.StructuredPostal.SORT_KEY_PRIMARY,
            cursorReader = { cursor -> cursor.map { getAddress(context)!! } }
    ) ?: emptyList()
}

fun <T> Context.queryContent(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        cursorReader: (cursor: Cursor) -> T?
): T? {
    val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    return if (cursor != null && cursor.count > 0) {
        val data = cursorReader(cursor)
        cursor.close()
        data
    } else
        null
}
