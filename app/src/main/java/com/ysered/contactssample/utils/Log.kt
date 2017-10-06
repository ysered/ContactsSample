@file:JvmName("LogUtils")

package com.ysered.contactssample.utils

import android.util.Log

fun Any.debug(text: String, t: Throwable? = null) {
    Log.d(this::class.java.simpleName, text, t)
}

fun Any.info(text: String, t: Throwable? = null) {
    Log.i(this::class.java.simpleName, text, t)
}

fun Any.warn(text: String, t: Throwable? = null) {
    Log.w(this::class.java.simpleName, text, t)
}

fun Any.error(text: String?, t: Throwable? = null) {
    Log.e(this::class.java.simpleName, text, t)
}
