package com.ysered.contactssample.details

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders


class ViewModelFactory(
        private val application: Application,
        private val contactId: String
) : ViewModelProviders.DefaultFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T = when (modelClass) {
        DetailsViewModel::class.java -> DetailsViewModel(application, contactId) as T
        else -> throw RuntimeException("Unknown model class: ${modelClass?.canonicalName}")
    }
}
