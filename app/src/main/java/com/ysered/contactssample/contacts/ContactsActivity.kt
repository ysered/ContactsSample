package com.ysered.contactssample.contacts

import android.Manifest
import android.arch.lifecycle.LifecycleOwner
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContact
import com.ysered.contactssample.details.DetailsActivity
import com.ysered.contactssample.utils.forEach
import com.ysered.contactssample.utils.processPermissionResults
import com.ysered.contactssample.utils.requestPermissionsIfNeeded
import com.ysered.contactssample.utils.showToast

class ContactsActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        private val REQUEST_CODE_CONTACTS = 1
        private val LOADER_ID = 1
        private val PROJECTION = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
    }

    private lateinit var contactsRv: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    private val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> = CursorLoader(
                this@ContactsActivity,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Contacts.SORT_KEY_PRIMARY
        )

        override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
            cursor?.let {
                val contacts = mutableListOf<Contact>()
                cursor.forEach { contacts.add(getContact()) }
                if (contacts.isNotEmpty())
                    contactsAdapter.contacts = contacts
            }
        }

        override fun onLoaderReset(loader: android.support.v4.content.Loader<Cursor>?) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        requestPermissionsIfNeeded(REQUEST_CODE_CONTACTS, this::onGranted, Manifest.permission.READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_CONTACTS -> processPermissionResults(permissions, grantResults, this::onGranted, this::onDenied)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun onGranted() {
        contactsAdapter = ContactsAdapter(object : ContactsAdapter.Callback {
            override fun onClick(contact: Contact) {
                DetailsActivity.start(this@ContactsActivity, contact)
            }
        })
        contactsRv = findViewById<RecyclerView>(R.id.contactsRv).apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            addItemDecoration(DividerItemDecoration(this@ContactsActivity, DividerItemDecoration.VERTICAL))
            adapter = contactsAdapter
        }
        supportLoaderManager.initLoader(LOADER_ID, null, loaderCallbacks)
    }

    private fun onDenied(isShowAdditionalHint: Boolean) {
        showToast(R.string.add_contacts_permission)
    }
}
