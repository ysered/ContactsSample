package com.ysered.contactssample.contacts

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.ysered.contactssample.data.Contact


class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsLiveData = MutableLiveData<List<Contact>>()

    private val onUpdateListener = object: ContactsObserver.OnUpdateListener {
        override fun onUpdated(contacts: List<Contact>) {
            contactsLiveData.postValue(contacts)
        }
    }

    private val contactsObserver = ContactsObserver(application.applicationContext, onUpdateListener)

    fun observeChanges(owner: LifecycleOwner, observer: Observer<List<Contact>>) {
        owner.lifecycle.addObserver(contactsObserver)
        contactsLiveData.observe(owner, observer)
    }
}
