package com.ysered.contactssample.details

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
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
    private val collapsingToolbar by lazy { findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (contact != null) {
            collapsingToolbar.title = contact!!.displayName
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact_details, menu)
        return true
    }
}
