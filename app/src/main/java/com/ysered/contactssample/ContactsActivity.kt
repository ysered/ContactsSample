package com.ysered.contactssample

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.contactssample.utils.debug

class ContactsActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        private val LOADER_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val contactsObserver = ContactsObserver(this).apply {
            contactsLiveData.observe(this@ContactsActivity, Observer { contacts ->
                debug("Observed contactsLiveData: $contacts")
            })
        }
        lifecycle.addObserver(contactsObserver)
        supportLoaderManager.initLoader(LOADER_ID, null, contactsObserver)
    }
}
