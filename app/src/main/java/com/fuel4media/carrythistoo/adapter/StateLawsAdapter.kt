package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.enums.LawStatus
import com.fuel4media.carrythistoo.model.StateLaws
import kotlinx.android.synthetic.main.row_item_law.view.*

class StateLawsAdapter(val context: Context, val laws: ArrayList<StateLaws>, val listener: StateLawsAdapter.LawClickListener) : RecyclerView.Adapter<StateLawsAdapter.ViewHolder>() {

    interface LawClickListener {
        fun onLawClick(law: StateLaws)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_law, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val law = this.laws[position]

        holder.law_name.text = law.law_name

        if (law.status == LawStatus.NO.type) {
            holder.law_status.text = "No"
           // holder.iv_status.setImageResource(R.drawable.red)
            ImageViewCompat.setImageTintList(holder.iv_status, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));

        } else if (law.status == LawStatus.YES.type) {
            holder.law_status.text = "Yes"
          //  holder.iv_status.setImageResource(R.drawable.blue)
            ImageViewCompat.setImageTintList(holder.iv_status, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimaryDark)));

        } else if (law.status == LawStatus.MAY_ISSUE.type) {
            holder.law_status.text = "May be"
           // holder.iv_status.setImageResource(R.drawable.grey_circle)
            ImageViewCompat.setImageTintList(holder.iv_status, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)));
        }
    }

    fun setBackground(layout: ImageView, drawble: Int) {
        val sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, drawble));
        } else {
            layout.setBackground(ContextCompat.getDrawable(context, drawble));
        }
    }

    override fun getItemCount(): Int {
        return laws.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val law_name = view!!.tv_law_name
        val law_status = view!!.tv_law_status
        val rl_parent = view!!.rl_parent
        val iv_status = view!!.iv_status

        init {
            view!!.setOnClickListener(View.OnClickListener {
                listener.onLawClick(laws.get(adapterPosition))
            })
        }
    }
}