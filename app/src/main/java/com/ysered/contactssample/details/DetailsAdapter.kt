package com.ysered.contactssample.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ysered.contactssample.R
import com.ysered.contactssample.data.Phone
import com.ysered.contactssample.utils.isVisible


class DetailsAdapter : RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    companion object {
        val TYPE_PHONE_ICON = 0
        val TYPE_PHONE = 1
    }

    var phones: List<Phone> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int = if (position == 0)
        TYPE_PHONE_ICON
    else
        TYPE_PHONE

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_phone, parent, false)
        return ViewHolder(view, isShowPhoneIcon = viewType == TYPE_PHONE_ICON)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(phones[position])
    }

    override fun getItemCount(): Int = phones.size

    inner class ViewHolder(itemView: View, isShowPhoneIcon: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val phoneText = itemView.findViewById<TextView>(R.id.phoneText)
        private val phoneTypeText = itemView.findViewById<TextView>(R.id.phoneTypeText)

        init {
            itemView.findViewById<ImageView>(R.id.phoneImage).apply { isVisible = isShowPhoneIcon }
        }

        fun bind(phone: Phone) {
            phoneText.text = phone.number
            phoneTypeText.text = phone.type
        }
    }
}