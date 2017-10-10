package com.ysered.contactssample.observers

import android.content.Context
import android.provider.ContactsContract
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.utils.ContentProviderUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async


class ContactListObserver(
        private val context: Context,
        onLoadListener: OnLoadListener<List<Contact>>
) : SystemDataObserver<List<Contact>>(context, ContactsContract.Contacts.CONTENT_URI, onLoadListener) {

    override suspend fun getData(): Deferred<List<Contact>> = async(CommonPool) {
        ContentProviderUtils.readContactList(this@ContactListObserver.context)
    }
}
