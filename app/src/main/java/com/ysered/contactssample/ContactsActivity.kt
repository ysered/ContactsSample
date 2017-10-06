package com.ysered.contactssample

import android.Manifest
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.contactssample.utils.debug
import com.ysered.contactssample.utils.processPermissionResults
import com.ysered.contactssample.utils.requestPermissionsIfNeeded
import com.ysered.contactssample.utils.showToast

class ContactsActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        private val LOADER_ID = 1
        private val RC_CONTACTS = 1
    }

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
        val contactsObserver = ContactsObserver(this).apply {
            contactsLiveData.observe(this@ContactsActivity, Observer { contacts ->
                debug("Observed contacts: $contacts")
            })
        }
        lifecycle.addObserver(contactsObserver)
        supportLoaderManager.initLoader(LOADER_ID, null, contactsObserver)
    }

    private fun onDenied(isShowAdditionalHint: Boolean) {
        showToast(R.string.add_contacts_permission)
    }
}
