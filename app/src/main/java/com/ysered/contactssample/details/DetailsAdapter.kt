package com.ysered.contactssample.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.ContactDetails
import com.ysered.contactssample.utils.isVisible


class DetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_SEPARATOR = 0
        val TYPE_SEPARATOR_LINE = 1
        val TYPE_PHONE = 2
        val TYPE_EMAIL = 3
        val TYPE_ADDRESS = 4
    }

    private sealed class Item(val itemType: Int) {
        class Separator : Item(TYPE_SEPARATOR)
        class SeparatorLine : Item(TYPE_SEPARATOR_LINE)
        sealed class Data(
                itemType: Int,
                val kind: String,
                val data: String,
                val isFirst: Boolean,
                val icon: Int,
                val associatedAppIcon: Int
        ) : Item(itemType) {
            class Phone(kind: String, data: String, isFirst: Boolean)
                : Data(TYPE_PHONE, kind, data, isFirst, R.drawable.ic_phone, R.drawable.ic_message)

            class Email(kind: String, data: String, isFirst: Boolean)
                : Data(TYPE_EMAIL, kind, data, isFirst, R.drawable.ic_email, 0)

            class Address(kind: String, data: String, isFirst: Boolean)
                : Data(TYPE_ADDRESS, kind, data, isFirst, R.drawable.ic_place, R.drawable.ic_directions)
        }
    }

    private var items: MutableList<Item> = mutableListOf()

    var contactDetails: ContactDetails? = null
        set(value) {
            value?.let {
                items.clear()
                if (it.phones.isNotEmpty()) {
                    items.add(Item.Separator())
                    it.phones.forEachIndexed { index, phone ->
                        items.add(Item.Data.Phone(phone.kind, phone.number, isFirst = index == 0))
                    }
                }
                if (it.emails.isNotEmpty()) {
                    items.add(Item.SeparatorLine())
                    it.emails.forEachIndexed { index, email ->
                        items.add(Item.Data.Email(email.kind, email.address, isFirst = index == 0))
                    }
                }
                if (it.addresses.isNotEmpty()) {
                    items.add(Item.SeparatorLine())
                    it.addresses.forEachIndexed { index, address ->
                        items.add(Item.Data.Address(address.kind, address.fullAddress, isFirst = index == 0))
                    }
                }
                notifyDataSetChanged()
            }
        }

    override fun getItemViewType(position: Int): Int = items[position].itemType

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            TYPE_SEPARATOR, TYPE_SEPARATOR_LINE -> {
                val layout = if (viewType == TYPE_SEPARATOR) R.layout.item_separator else R.layout.item_separator_line
                val view = inflater.inflate(layout, parent, false)
                SeparatorViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_details_data, parent, false)
                ItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val item = items[position]
        if (item.itemType != TYPE_SEPARATOR) {
            (holder as? ItemViewHolder)?.bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    private inner class SeparatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dataIcon = itemView.findViewById<ImageView>(R.id.dataIconImage)
        private val dataKindText = itemView.findViewById<TextView>(R.id.dataKindText)
        private val dataText = itemView.findViewById<TextView>(R.id.dataText)
        private val appIconImage = itemView.findViewById<ImageView>(R.id.appIconImage)

        fun bind(item: Item) {
            when (item) {
                is Item.Data -> {
                    dataIcon.setImageResource(item.icon)
                    dataIcon.isVisible = item.isFirst
                    dataText.text = item.data
                    dataKindText.text = item.kind
                    if (item is Item.Data.Email)
                        appIconImage.isVisible = false
                    else
                        appIconImage.setImageResource(item.associatedAppIcon)
                }
                else -> throw RuntimeException("Unknown item type: ${item::class}")
            }
        }
    }
}