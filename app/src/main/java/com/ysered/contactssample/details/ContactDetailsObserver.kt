package com.ysered.contactssample.details

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Data
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.ysered.contactssample.data.Phone
import com.ysered.contactssample.utils.forEach
import com.ysered.contactssample.utils.getInt
import com.ysered.contactssample.utils.getPhoneTypeName
import com.ysered.contactssample.utils.getString


class ContactDetailsObserver(
        private val context: Context,
        private val contactId: String
) : LifecycleObserver, LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        val PHONES_LOADER = 0

        private val PHONES_SELECTION = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        private val PHONES_SORT_ORDER = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY
    }

    val phonesData = MutableLiveData<List<Phone>>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = when (id) {
        PHONES_LOADER -> {
            val selectionArgs = arrayOf(contactId)
            CursorLoader(
                    context,
                    Data.CONTENT_URI,
                    null,
                    PHONES_SELECTION,
                    selectionArgs,
                    PHONES_SORT_ORDER
            )
        }
        else -> throw RuntimeException("Unknown loader id: $id")
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (loader != null && data != null) {
            when (loader.id) {
                PHONES_LOADER -> {
                    val phones = mutableListOf<Phone>()
                    data.forEach {
                        val type = getInt(ContactsContract.CommonDataKinds.Phone.TYPE)
                        val typeName = getPhoneTypeName(context, type)
                        typeName?.let {
                            val number = getString(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            phones.add(Phone(typeName, number))
                        }
                    }
                    if (phones.isNotEmpty())
                        phonesData.postValue(phones)
                }
                else -> throw RuntimeException("Unknown loader id: ${loader.id}")
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {

    }
}