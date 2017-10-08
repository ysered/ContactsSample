package com.ysered.contactssample.details

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.*
import com.ysered.contactssample.utils.forEach
import com.ysered.contactssample.utils.showToast


class DetailsActivity : AppCompatActivity() {

    companion object {
        private val PHONES_LOADER = 0
        private val PHONES_SELECTION = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        private val PHONES_SORT_ORDER = ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY

        fun start(context: Context, contact: Contact) {
            val intent = Intent(context, DetailsActivity::class.java).putExtra(contact)
            context.startActivity(intent)
        }
    }

    private val contact by lazy { intent?.getContactExtra() }

    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var photoImage: ImageView
    private lateinit var detailsAdapter: DetailsAdapter

    private val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = when (id) {
            PHONES_LOADER -> {
                val selectionArgs = arrayOf(contact!!.id)
                CursorLoader(
                        this@DetailsActivity,
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        PHONES_SELECTION,
                        selectionArgs,
                        PHONES_SORT_ORDER
                )
            }
            else -> throw RuntimeException("Unknown loader id: $id")
        }

        override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
            if (loader != null && cursor != null) {
                when (loader.id) {
                    PHONES_LOADER -> {
                        val phones = mutableListOf<Phone>()
                        cursor.forEach {
                            getPhone(this@DetailsActivity)?.let { phones.add(it) }
                        }
                        if (phones.isNotEmpty())
                            detailsAdapter.phones = phones
                    }
                    else -> throw RuntimeException("Unknown loader id: ${loader.id}")
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>?) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // init views
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        toolbar = findViewById(R.id.toolbar)
        photoImage = findViewById(R.id.contactPhotoImage)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailsAdapter = DetailsAdapter()
        findViewById<RecyclerView>(R.id.contactDataRv).apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(this@DetailsActivity)
        }

        if (contact != null) {
            showContact(contact!!)
            initPhonesLoader()
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

    private fun initPhonesLoader() {
        if (contact!!.hasPhoneNumber) {
            supportLoaderManager.initLoader(PHONES_LOADER, null, loaderCallbacks)
        }
    }
}
