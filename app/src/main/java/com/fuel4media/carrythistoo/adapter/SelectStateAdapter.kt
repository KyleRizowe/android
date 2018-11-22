package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.State

class SelectStateAdapter(val context: Context, var states: ArrayList<State>, val callback: SelectStateAdapter.OnClickListener) : RecyclerView.Adapter<SelectStateAdapter.ViewHolder>() {


    interface OnClickListener {
        fun onStateClick(state: State)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_select_state, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state = this.states[position]

        holder.tvName!!.text = state.state_name

        holder.itemView.setOnClickListener(View.OnClickListener {
            callback.onStateClick(state)
        })
    }

    override fun getItemCount(): Int {
        return states.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView? = null

        init {
            tvName = view!!.findViewById(R.id.tv_name)
        }
    }
}