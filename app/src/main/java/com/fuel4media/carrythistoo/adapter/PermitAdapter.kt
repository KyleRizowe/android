package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.Permit
import com.fuel4media.carrythistoo.utils.GlideUtil
import kotlinx.android.synthetic.main.row_permit.view.*

class PermitAdapter(val context: Context, val permits: ArrayList<Permit>, val listener: PermitClickListener) : RecyclerView.Adapter<PermitAdapter.ViewHolder>() {

    interface PermitClickListener {
        fun onPermitClick(permit: Permit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_permit, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val permit = this.permits[position]

        holder.permit_id.text = String.format(context.getString(R.string.permit_id), permit.permit_id)
        GlideUtil.loadImage(context, holder.permit_image, permit.permit_image)
        holder.permit_state.text = String.format(context.getString(R.string.state), permit.state_name)
        holder.permit_type.text = String.format(context.getString(R.string.permit_type), permit.permit_name)
    }

    override fun getItemCount(): Int {
        return permits.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val permit_image = view!!.iv_permit
        val permit_id = view!!.tv_permit_id
        val permit_type = view!!.tv_permit_type
        val permit_state = view!!.tv_state

        init {
            view!!.setOnClickListener(View.OnClickListener {
                listener.onPermitClick(permits.get(adapterPosition))
            })
        }
    }

}