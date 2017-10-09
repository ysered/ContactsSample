package com.ysered.contactssample.contacts

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.ContactsContract
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContact
import com.ysered.contactssample.utils.map


class ContactsObserver(context: Context, private val listener: OnUpdateListener) : LifecycleObserver {

    companion object {
        private val URI = ContactsContract.Contacts.CONTENT_URI
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        contentResolver.registerContentObserver(URI, false, observer)
        queryAndUpdateContacts()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        contentResolver.unregisterContentObserver(observer)
    }

    private fun queryAndUpdateContacts() {
        contentResolver.query(URI, PROJECTION, SELECTION, SELECTION_ARGS, ORDER_BY).let { cursor ->
            val contacts = cursor.map { getContact() }
            cursor.close()
            listener.onUpdated(contacts)
        }
    }

    interface OnUpdateListener {
        fun onUpdated(contacts: List<Contact>)
    }
}
