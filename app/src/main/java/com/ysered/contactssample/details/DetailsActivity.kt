package com.ysered.contactssample.details

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.data.getContactExtra
import com.ysered.contactssample.data.putExtra
import com.ysered.contactssample.utils.showToast
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, contact: Contact) {
            val intent = Intent(context, DetailsActivity::class.java).putExtra(contact)
            context.startActivity(intent)
        }
    }

    private val contact by lazy { intent?.getContactExtra() }
    private val detailsAdapter by lazy { DetailsAdapter() }
    private val viewModelFactory by lazy { ViewModelFactory(application, contact!!.id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contactDataRv.apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(this@DetailsActivity)
        }

        if (contact != null) {
            showContact(contact!!)
            val viewModel = viewModelFactory.create(DetailsViewModel::class.java)
            viewModel.observeChanges(this, Observer { details ->
                details?.let {
                    collapsingToolbar.title = it.displayName
                    detailsAdapter.contactDetails = it
                }
            })
        } else {
            showToast(R.string.cannot_find_contact)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact_details, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showContact(contact: Contact) {
        collapsingToolbar.title = contact.displayName
        if (contact.photoUri != null)
            photoImage.setImageURI(contact.photoUri)
        else
            photoImage.setImageResource(R.drawable.bg_contact)
    }
}
