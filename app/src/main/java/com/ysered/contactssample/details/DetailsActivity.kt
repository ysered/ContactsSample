package com.ysered.contactssample.details

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.ysered.contactssample.R
import com.ysered.contactssample.data.*
import com.ysered.contactssample.utils.first
import com.ysered.contactssample.utils.map
import com.ysered.contactssample.utils.showToast
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    companion object {
        private val CONTACT_LOADER = 0
        private val PHONES_LOADER = 1
        private val EMAILS_LOADER = 2
        private val ADDRESSES_LOADER = 3

        private val CONTACT_SELECTION = "${ContactsContract.Contacts._ID} = ?"

        private val PHONES_SELECTION = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        private val PHONES_SORT_ORDER = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY

        private val ADDRESSES_SELECTION = "${ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID} = ?"
        private val ADDRESSES_SORT_ORDER = ContactsContract.CommonDataKinds.StructuredPostal.SORT_KEY_PRIMARY

        private val EMAILS_SELECTION = "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?"
        private val EMAILS_SORT_ORDER = ContactsContract.CommonDataKinds.Email.SORT_KEY_PRIMARY

        fun start(context: Context, contact: Contact) {
            val intent = Intent(context, DetailsActivity::class.java).putExtra(contact)
            context.startActivity(intent)
        }
    }

    private val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            val selectionArgs = arrayOf(contact!!.id)
            return when (id) {
                CONTACT_LOADER -> {
                    CursorLoader(
                            this@DetailsActivity,
                            ContactsContract.Contacts.CONTENT_URI,
                            null,
                            CONTACT_SELECTION,
                            selectionArgs,
                            null)
                }
                PHONES_LOADER -> {
                    CursorLoader(
                            this@DetailsActivity,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            PHONES_SELECTION,
                            selectionArgs,
                            PHONES_SORT_ORDER)
                }
                EMAILS_LOADER -> {
                    CursorLoader(
                            this@DetailsActivity,
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            EMAILS_SELECTION,
                            selectionArgs,
                            EMAILS_SORT_ORDER)
                }
                ADDRESSES_LOADER -> {
                    CursorLoader(
                            this@DetailsActivity,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                            null,
                            ADDRESSES_SELECTION,
                            selectionArgs,
                            ADDRESSES_SORT_ORDER)
                }
                else -> throw RuntimeException("Unknown loader id: $id")
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
            if (loader != null && cursor != null) {
                when (loader.id) {
                    CONTACT_LOADER -> {
                        cursor.first {
                            getContact().let { collapsingToolbar.title = it.displayName }
                        }
                    }
                    PHONES_LOADER -> {
                        detailsAdapter.phones = cursor.map { getPhone(this@DetailsActivity)!! }
                    }
                    EMAILS_LOADER -> {
                        detailsAdapter.emails = cursor.map { getEmail(this@DetailsActivity)!! }
                    }
                    ADDRESSES_LOADER -> {
                        detailsAdapter.addresses = cursor.map { getAddress(this@DetailsActivity)!! }
                    }
                    else -> throw RuntimeException("Unknown loader id: ${loader.id}")
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>?) = Unit
    }

    private val contact by lazy { intent?.getContactExtra() }
    private val detailsAdapter by lazy { DetailsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contactDataRv.apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(this@DetailsActivity)
        }

        if (contact != null) {
            showContact(contact!!)
            supportLoaderManager.initLoader(CONTACT_LOADER, null, loaderCallbacks)
            supportLoaderManager.initLoader(PHONES_LOADER, null, loaderCallbacks)
            supportLoaderManager.initLoader(EMAILS_LOADER, null, loaderCallbacks)
            supportLoaderManager.initLoader(ADDRESSES_LOADER, null, loaderCallbacks)
        } else {
            showToast(R.string.cannot_find_contact)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact_details, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showContact(contact: Contact) {
        collapsingToolbar.title = contact.displayName
        if (contact.photoUri != null)
            photoImage.setImageURI(contact.photoUri)
        else
            photoImage.setImageResource(R.drawable.bg_contact)
    }
}
