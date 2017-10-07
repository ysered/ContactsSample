package com.ysered.contactssample.contacts

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContact
import com.ysered.contactssample.utils.forEach


class ContactsObserver(
        private val context: Context
) : LifecycleObserver, LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private val PROJECTION = arrayOf(
                Contacts._ID,
                Contacts.PHOTO_THUMBNAIL_URI,
                Contacts.PHOTO_URI,
                Contacts.DISPLAY_NAME,
                Contacts.HAS_PHONE_NUMBER
        )
    }

    val contactsLiveData = MutableLiveData<List<Contact>>()

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> = CursorLoader(
            context,
            Contacts.CONTENT_URI,
            PROJECTION,
            null,
            null,
            Contacts.SORT_KEY_PRIMARY
    )

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        cursor?.let {
            val contactList = mutableListOf<Contact>()
            it.forEach { contactList.add(getContact()) }
            if (contactList.isNotEmpty())
                contactsLiveData.postValue(contactList)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) = Unit
}
