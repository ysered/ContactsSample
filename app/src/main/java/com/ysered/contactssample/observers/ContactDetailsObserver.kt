package com.ysered.contactssample.observers

import android.content.Context
import android.provider.ContactsContract
import com.ysered.contactssample.data.ContactDetails
import com.ysered.contactssample.utils.ContentProviderUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async


class ContactDetailsObserver(
        private val context: Context,
        private val contactId: String,
        onLoadListener: OnLoadListener<ContactDetails>
) : SystemDataObserver<ContactDetails>(context, ContactsContract.Contacts.CONTENT_URI, onLoadListener) {

    override suspend fun getData(): Deferred<ContactDetails> = async(CommonPool) {
        val context = this@ContactDetailsObserver.context
        val contact = ContentProviderUtils.readContact(context, contactId)
        val phones = ContentProviderUtils.readPhoneList(context, contactId)
        val emails = ContentProviderUtils.readEmailList(context, contactId)
        val addresses = ContentProviderUtils.readAddressList(context, contactId)
        ContactDetails(contact!!.displayName, phones, emails, addresses)
    }
}
