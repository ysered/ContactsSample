package com.ysered.contactssample.data

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.ysered.contactssample.utils.getString
import com.ysered.contactssample.utils.getUri

data class Contact(
        val id: String,
        val thumbnailUri: Uri?,
        val photoUri: Uri?,
        val displayName: String
)

private object Constants {
    val EXTRA_CONTACT = "extra_contact"
    val EXTRA_ID = "extra_contact_id"
    val EXTRA_THUMBNAIL_URI = "extra_contact_thumbnail_uri"
    val EXTRA_PHOTO_URI = "extra_contact_photo_uri"
    val EXTRA_DISPLAY_NAME = "extra_contact_display_name"
}

fun Intent.putExtra(contact: Contact): Intent {
    putExtra(Constants.EXTRA_CONTACT, "contact")
    putExtra(Constants.EXTRA_ID, contact.id)
    putExtra(Constants.EXTRA_THUMBNAIL_URI, if (contact.thumbnailUri != null) contact.thumbnailUri.toString() else null)
    putExtra(Constants.EXTRA_PHOTO_URI, if (contact.photoUri != null) contact.photoUri.toString() else null)
    putExtra(Constants.EXTRA_DISPLAY_NAME, contact.displayName)
    return this
}

fun Intent.getContactExtra(): Contact? = if (extras.containsKey(Constants.EXTRA_CONTACT)) {
    extras.run {
        val id = getString(Constants.EXTRA_ID)
        val thumbnailUriString = getString(Constants.EXTRA_THUMBNAIL_URI)
        val thumbnailUri = if (thumbnailUriString != null) Uri.parse(thumbnailUriString) else null
        val photoUriString = getString(Constants.EXTRA_PHOTO_URI)
        val photoUri = if (photoUriString != null) Uri.parse(photoUriString) else null
        val displayName = getString(Constants.EXTRA_DISPLAY_NAME)
        Contact(id, thumbnailUri, photoUri, displayName)
    }
} else
    null

/**
 * Reads data contacts content provider into [Contact] object.
 */
fun Cursor.getContact(): Contact {
    val id = getString(ContactsContract.Contacts._ID)
    val thumbnailUri = getUri(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
    val photoUri = getUri(ContactsContract.Contacts.PHOTO_URI)
    val displayName = getString(ContactsContract.Contacts.DISPLAY_NAME)
    return Contact(id, thumbnailUri, photoUri, displayName)
}
