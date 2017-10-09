package com.ysered.contactssample.observers

import android.content.Context
import android.provider.ContactsContract
import com.ysered.contactssample.data.ContactDetails
import com.ysered.contactssample.utils.ContentProviderUtils


class ContactDetailsObserver(
        private val context: Context,
        private val contactId: String,
        callback: (data: ContactDetails) -> Unit
) : SystemDataObserver<ContactDetails>(
        context,
        uri = ContactsContract.Contacts.CONTENT_URI,
        callback = callback
) {

    override fun getData(): ContactDetails {
        val contact = ContentProviderUtils.readContact(context, contactId)
        val phones = ContentProviderUtils.readPhoneList(context, contactId)
        val emails = ContentProviderUtils.readEmailList(context, contactId)
        val addresses = ContentProviderUtils.readAddressList(context, contactId)
        return ContactDetails(contact!!.displayName, phones, emails, addresses)
    }
}
