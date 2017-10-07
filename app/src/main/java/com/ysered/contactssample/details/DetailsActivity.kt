package com.ysered.contactssample.details

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContactExtra
import com.ysered.contactssample.data.putExtra
import com.ysered.contactssample.utils.info
import com.ysered.contactssample.utils.showToast


class DetailsActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, contact: Contact) {
            val intent = Intent(context, DetailsActivity::class.java).putExtra(contact)
            context.startActivity(intent)
        }
    }

    private val contact by lazy { intent?.getContactExtra() }
    private val photoImage by lazy { findViewById<ImageView>(R.id.contactPhotoImage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (contact != null) {
            photoImage.setImageURI(contact!!.photoUri)
            val observer = ContactDetailsObserver(this, contact!!.id).apply {
                phonesData.observe(this@DetailsActivity, Observer { phones ->
                    info("Received phones: $phones")
                })
            }
            lifecycle.addObserver(observer)
            supportLoaderManager.initLoader(ContactDetailsObserver.PHONES_LOADER, null, observer)
        } else {
            showToast(R.string.cannot_find_contact)
            finish()
        }
    }
}
