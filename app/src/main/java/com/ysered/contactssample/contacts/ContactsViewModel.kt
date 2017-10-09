package com.ysered.contactssample.contacts

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.observers.ContactListObserver


class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsLiveData = MutableLiveData<List<Contact>>()
    private val contactsObserver = ContactListObserver(application.applicationContext, { contacts ->
        contactsLiveData.postValue(contacts)
    })

    fun observeChanges(owner: LifecycleOwner, observer: Observer<List<Contact>>) {
        owner.lifecycle.addObserver(contactsObserver)
        contactsLiveData.observe(owner, observer)
    }
}
