package com.ysered.contactssample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ysered.contactssample.data.Contact
import com.ysered.contactssample.views.ContactImageView


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    var contacts: List<Contact> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder?, position: Int) {
        holder?.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImage = itemView.findViewById<ContactImageView>(R.id.photoImage)
        private val displayNameText = itemView.findViewById<TextView>(R.id.displayNameText)

        init {
            // TODO: find better way
            setIsRecyclable(false)
        }

        fun bind(contact: Contact) {
            if (contact.photoUri == null)
                photoImage.firstLetter = contact.displayName.first().toString()
            else
                photoImage.setImageURI(contact.photoUri)
            displayNameText.text = contact.displayName
        }
    }
}
