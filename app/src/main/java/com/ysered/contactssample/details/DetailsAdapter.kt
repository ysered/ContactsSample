package com.ysered.contactssample.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Email
import com.ysered.contactssample.data.Phone
import com.ysered.contactssample.utils.isVisible


class DetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_SEPARATOR = 0
        val TYPE_PHONE = 1
        val TYPE_EMAIL = 2
    }

    private sealed class Item(val itemType: Int) {
        class Separator : Item(TYPE_SEPARATOR)
        class Phone(val kind: String, val data: String, val isFirst: Boolean = false) : Item(TYPE_PHONE)
        class Email(val kind: String, val data: String, val isFirst: Boolean = false) : Item(TYPE_EMAIL)
    }

    private val lastPosition: Int
        get() = if (itemCount > 0) itemCount - 1 else 0

    var phones: List<Phone> = emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                items.add(Item.Separator())
                notifyItemInserted(lastPosition)
                value.forEachIndexed { index, phone ->
                    items.add(Item.Phone(phone.kind, phone.number, isFirst = index == 0))
                    notifyItemInserted(lastPosition)
                }
            }
        }

    var emails: List<Email> = emptyList()
        set(value) {
            field = value
            items.add(Item.Separator())
            notifyItemInserted(lastPosition)
            value.forEachIndexed { index, email ->
                items.add(Item.Email(email.kind, email.address, isFirst = index == 0))
                notifyItemInserted(lastPosition)
            }
        }

    private var items: MutableList<Item> = mutableListOf()

    override fun getItemViewType(position: Int): Int = items[position].itemType

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return if (viewType == TYPE_SEPARATOR) {
            val view = inflater.inflate(R.layout.item_separator, parent, false)
            SeparatorViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_details_data, parent, false)
            ItemViewHolder(view)
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
                is Item.Phone -> {
                    dataIcon.setImageResource(R.drawable.ic_phone)
                    appIconImage.setImageResource(R.drawable.ic_message)
                    dataIcon.isVisible = item.isFirst
                    dataText.text = item.data
                    dataKindText.text = item.kind
                }
                is Item.Email -> {
                    dataIcon.setImageResource(R.drawable.ic_email)
                    appIconImage.isVisible = false
                    dataIcon.isVisible = item.isFirst
                    dataText.text = item.data
                    dataKindText.text = item.kind
                }
                else -> throw RuntimeException("Unknown item type: ${item::class}")
            }

        }
    }
}