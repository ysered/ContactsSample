package com.ysered.contactssample.details

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.ysered.contactssample.data.ContactDetails
import com.ysered.contactssample.observers.ContactDetailsObserver


class DetailsViewModel(application: Application, contactId: String) : AndroidViewModel(application) {

    private val contactDetailsData = MutableLiveData<ContactDetails>()
    private val contactDetailsObserver = ContactDetailsObserver(application.applicationContext, contactId,
            callback = { details ->
                contactDetailsData.postValue(details)
            })

    fun observeChanges(owner: LifecycleOwner, observer: Observer<ContactDetails>) {
        owner.lifecycle.addObserver(contactDetailsObserver)
        contactDetailsData.observe(owner, observer)
    }
}
