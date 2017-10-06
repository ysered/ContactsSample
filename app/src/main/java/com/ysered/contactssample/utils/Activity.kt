@file:JvmName("ActivityUtils")

package com.ysered.contactssample.utils

import android.app.Activity
import android.widget.Toast


fun Activity.showToast(textResId: Int) {
    Toast.makeText(this, textResId, Toast.LENGTH_SHORT).show()
}
