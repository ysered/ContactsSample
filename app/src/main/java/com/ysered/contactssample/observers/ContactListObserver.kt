package com.ysered.contactssample.observers

import android.content.Context
import android.provider.ContactsContract
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.utils.ContentProviderUtils


class ContactListObserver(
        private val context: Context,
        callback: (data: List<Contact>) -> Unit
) : SystemDataObserver<List<Contact>>(
        context,
        uri = ContactsContract.Contacts.CONTENT_URI,
        callback = callback
) {

    override fun getData(): List<Contact> = ContentProviderUtils.readContactList(context)
}
