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
import com.ysered.contactssample.utils.forEach
import com.ysered.contactssample.utils.getString
import com.ysered.contactssample.utils.getUri


class ContactsObserver(
        private val context: Context
) : LifecycleObserver, LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private val PROJECTION = arrayOf(Contacts.PHOTO_URI, Contacts.DISPLAY_NAME)
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
            it.forEach {
                val photoUri = getUri(Contacts.PHOTO_URI)
                val displayName = getString(Contacts.DISPLAY_NAME)
                contactList.add(Contact(photoUri, displayName))
            }
            contactsLiveData.postValue(contactList)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) = Unit
}
