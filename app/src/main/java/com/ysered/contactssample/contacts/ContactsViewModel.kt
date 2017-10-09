package com.ysered.contactssample.contacts

import android.app.Application
import android.arch.lifecycle.AndroidViewModel


class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    val contactsLiveData = ContactsLiveData(application.applicationContext)
}
