package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.State

class StateAdapter(val context: Context, var states: ArrayList<State>, val callback: StateAdapter.OnClickListener) : RecyclerView.Adapter<StateAdapter.ViewHolder>() {
    var contactListFiltered = ArrayList<State>()

    init {
        this.contactListFiltered = states
    }

    fun updateList(filterList: ArrayList<State>) {
        states = filterList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onCheckBoxClick(value: Boolean, contact: State)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_contact, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = this.states[position]

        holder.tvName!!.text = contact.state_name

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
        return states.size
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