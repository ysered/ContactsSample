package com.ysered.contactssample.contacts

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.ContactsContract
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContact
import com.ysered.contactssample.utils.map


class ContactsLiveData(context: Context) : MutableLiveData<List<Contact>>() {

    companion object {
        private val CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI
        private val PROJECTION = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        private val SELECTION = null
        private val SELECTION_ARGS = null
        private val ORDER_BY = ContactsContract.Contacts.SORT_KEY_PRIMARY
    }

    private val contentResolver = context.contentResolver

    private val observer = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            queryAndUpdateContacts()
        }
    }

    override fun onActive() {
        super.onActive()
        contentResolver.registerContentObserver(CONTACTS_URI, false, observer)
        queryAndUpdateContacts()
    }

    override fun onInactive() {
        super.onInactive()
        contentResolver.unregisterContentObserver(observer)
    }

    private fun queryAndUpdateContacts() {
        contentResolver.query(CONTACTS_URI, PROJECTION, SELECTION, SELECTION_ARGS, ORDER_BY).apply {
            val contacts = map { getContact() }
            postValue(contacts)
            close()
        }
    }
}
