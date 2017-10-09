package com.ysered.contactssample.contacts

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.details.DetailsActivity
import com.ysered.contactssample.utils.processPermissionResults
import com.ysered.contactssample.utils.requestPermissionsIfNeeded
import com.ysered.contactssample.utils.showToast
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_CODE_CONTACTS = 1
    }

    private lateinit var contactsAdapter: ContactsAdapter

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
        contactsRv.apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            addItemDecoration(DividerItemDecoration(this@ContactsActivity, DividerItemDecoration.VERTICAL))
            adapter = contactsAdapter
        }
        val viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        viewModel.contactsLiveData.observe(this, Observer { contacts ->
            contacts?.let { contactsAdapter.contacts = it }
        })
    }

    private fun onDenied(isShowAdditionalHint: Boolean) {
        showToast(R.string.add_contacts_permission)
    }
}
