package com.ysered.contactssample.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

/**
 * Requests permissions if some of them aren't granted.
 *
 * @param [requestCode] to filter results in [Activity.onRequestPermissionsResult]
 * @param [onGranted] function to be executed if permissions already granted
 * @param [permissions] array of permission to be requested if some of them aren't grated
 */
inline fun Activity.requestPermissionsIfNeeded(requestCode: Int, onGranted: () -> Unit, vararg permissions: String) {
    val toRequest = permissions
            .filterNot { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
            .toTypedArray()
    if (toRequest.isNotEmpty())
        ActivityCompat.requestPermissions(this, toRequest, requestCode)
    else
        onGranted()
}

/**
 * Helper function to process permission results in [Activity.onRequestPermissionsResult].
 *
 * @param [permissions] list of requested permissions
 * @param [grantResults] flags to indicate which permissions is granted
 * @param [onGranted] function to be executed if all permissions is granted
 * @param [onDenied] function to be executed if some or all permissions is denied
 */
inline fun Activity.processPermissionResults(permissions: Array<out String>,
                                             grantResults: IntArray,
                                             onGranted: () -> Unit,
                                             onDenied: (Boolean) -> Unit) {
    if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
        onGranted()
    } else {
        val showHint = permissions.any { !ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
        onDenied(showHint)
    }
}
