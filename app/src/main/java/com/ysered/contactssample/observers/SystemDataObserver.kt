package com.ysered.contactssample.observers

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


abstract class SystemDataObserver<out T>(
        context: Context,
        private val uri: Uri,
        private val onLoadListener: OnLoadListener<T>
) : LifecycleObserver {

    private val contentResolver = context.contentResolver

    private val observer = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            getAndNotify()
        }
    }

    private var loadingJob: Job? = null

    suspend abstract fun getData(): Deferred<T>

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        contentResolver.registerContentObserver(uri, false, observer)
        getAndNotify()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        loadingJob?.let {
            if (it.isActive)
                it.cancel()
        }
        contentResolver.unregisterContentObserver(observer)
    }

    private fun getAndNotify() {
        loadingJob = launch(UI) {
            onLoadListener.onStartLoading()
            val data = getData().await()
            onLoadListener.onLoaded(data)
        }
    }

    interface OnLoadListener<in T> {
        fun onStartLoading()
        fun onLoaded(data: T)
    }
}