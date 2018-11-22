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
import com.fuel4media.carrythistoo.fragments.HomeFragment
import com.fuel4media.carrythistoo.model.PermitType

class SelectPermitAdapter(val context: Context, var permits: ArrayList<PermitType>, val callback: OnClickListener) : RecyclerView.Adapter<SelectPermitAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onCheckBoxClick(value: Boolean, contact: PermitType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_select_permit, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val permit = this.permits[position]

        holder.tvName!!.text = permit.permit_name

        holder.cbContact?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            callback.onCheckBoxClick(b, permit)

        })
    }

    override fun getItemCount(): Int {
        return permits.size
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