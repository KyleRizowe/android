package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.Contact


/**
 * Created by shweta on 31/5/18.
 */
class ContactApapter(val context: Context, var contacts: ArrayList<Contact>, val callback: OnClickListener) : RecyclerView.Adapter<ContactApapter.ViewHolder>() {
    var contactListFiltered = ArrayList<Contact>()

    init {
        this.contactListFiltered = contacts
    }

    fun updateList(filterList: ArrayList<Contact>) {
        contacts = filterList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onCheckBoxClick(value: Boolean, contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_contact, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = this.contacts[position]

        holder.tvName!!.text = contact.name

        holder.cbContact!!.isChecked = contact.status!!

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (contact.status!!) {
                contact.status = false
                holder.cbContact!!.isChecked = false
            } else {
                contact.status = true
                holder.cbContact!!.isChecked = true
            }

            callback.onCheckBoxClick(contact.status!!, contact)
        })

        /*     holder.cbContact?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                 callback.onCheckBoxClick(b, contact)
                 contact.status = b
                 holder.cbContact!!.isChecked = b
             })*/
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView? = null
        var cbContact: CheckBox? = null

        init {
            tvName = view!!.findViewById(R.id.tv_name)
            cbContact = view!!.findViewById(R.id.cb_contact)

        }
    }

}