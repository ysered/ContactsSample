package com.ysered.contactssample.contacts

import android.Manifest
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.details.DetailsActivity
import com.ysered.contactssample.utils.debug
import com.ysered.contactssample.utils.processPermissionResults
import com.ysered.contactssample.utils.requestPermissionsIfNeeded
import com.ysered.contactssample.utils.showToast

class ContactsActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        private val LOADER_ID = 1
        private val RC_CONTACTS = 1
    }

    private lateinit var contactsRv: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        requestPermissionsIfNeeded(RC_CONTACTS, this::onGranted, Manifest.permission.READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_CONTACTS -> processPermissionResults(permissions, grantResults, this::onGranted, this::onDenied)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun onGranted() {
        contactsAdapter = ContactsAdapter(object: ContactsAdapter.Callback {
            override fun onClick(contact: Contact) {
                DetailsActivity.start(this@ContactsActivity, contact)
            }
        })
        contactsRv = findViewById<RecyclerView>(R.id.contactsRv).apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            addItemDecoration(DividerItemDecoration(this@ContactsActivity, DividerItemDecoration.VERTICAL))
            adapter = contactsAdapter
        }

        val contactsObserver = ContactsObserver(this).apply {
            contactsLiveData.observe(this@ContactsActivity, Observer { contacts ->
                debug("Observed contacts: $contacts")
                contacts?.let { contactsAdapter.contacts = it }
            })
        }
        lifecycle.addObserver(contactsObserver)
        supportLoaderManager.initLoader(LOADER_ID, null, contactsObserver)
    }

    private fun onDenied(isShowAdditionalHint: Boolean) {
        showToast(R.string.add_contacts_permission)
    }
}
