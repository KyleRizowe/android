package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.FilterType

class FilterAdapter(val context: Context, var filters: ArrayList<FilterType>, val callback: FilterAdapter.OnClickListener) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {


    interface OnClickListener {
        fun onCheckBoxClick(value: Boolean, contact: FilterType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_filter_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = this.filters[position]

        holder.tvName!!.text = filter.filter_name

        holder.cbContact?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            callback.onCheckBoxClick(b, filter)

        })
    }

    override fun getItemCount(): Int {
        return filters.size
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