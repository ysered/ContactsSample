package com.ysered.contactssample.observers

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler


abstract class SystemDataObserver<out T>(
        context: Context,
        private val uri: Uri,
        private val callback: (data: T) -> Unit
) : LifecycleObserver {

    private val contentResolver = context.contentResolver

    private val observer = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            getAndNotify()
        }
    }

    abstract fun getData(): T

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        contentResolver.registerContentObserver(uri, false, observer)
        getAndNotify()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        contentResolver.unregisterContentObserver(observer)
    }

    private fun getAndNotify() {
        getData()?.let { data -> callback(data) }
    }
}