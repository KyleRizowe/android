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
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.enums.LawStatus
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.StateLaws
import kotlinx.android.synthetic.main.row_compare_laws.view.*

class CompareStateLawsAdpater(val context: Context, val laws: ArrayList<StateLaws>, val listener: CompareStateLawsAdpater.LawClickListener) : RecyclerView.Adapter<CompareStateLawsAdpater.ViewHolder>() {

    var state1: String? = null
    var state2: String? = null

    fun updateState1(state1: String) {
        this.state1 = state1
    }

    fun updateState2(state2: String) {
        this.state2 = state2
    }

    interface LawClickListener {
        fun onLawClick(law: StateLaws)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_compare_laws, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val law = this.laws[position]

        holder.law_name.text = law.law_name

        holder.tv_state1.text = law.state1_abv
        holder.tv_state2.text = law.state2_abv

        if (law.status1 == LawStatus.NO.type) {
            holder.staus1.text = "No"
            ImageViewCompat.setImageTintList(holder.iv_status1, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
            //holder.iv_status1.setImageResource(R.drawable.red)


        } else if (law.status1 == LawStatus.YES.type) {
            holder.staus1.text = "Yes"
            ImageViewCompat.setImageTintList(holder.iv_status1, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
            //holder.iv_status1.setImageResource(R.drawable.blue)


        } else if (law.status1 == LawStatus.MAY_ISSUE.type) {
            holder.staus1.text = "May be"
            ImageViewCompat.setImageTintList(holder.iv_status1, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)));
            //holder.iv_status1.setImageResource(R.drawable.grey_circle)
        }

        if (law.status2 == LawStatus.NO.type) {
            holder.staus2.text = "No"
            ImageViewCompat.setImageTintList(holder.iv_status2, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
            //holder.iv_status2.setImageResource(R.drawable.red)

        } else if (law.status2 == LawStatus.YES.type) {
            holder.staus2.text = "Yes"
            ImageViewCompat.setImageTintList(holder.iv_status2, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
            //holder.iv_status2.setImageResource(R.drawable.blue)

        } else if (law.status2 == LawStatus.MAY_ISSUE.type) {
            holder.staus2.text = "May be"
            ImageViewCompat.setImageTintList(holder.iv_status2, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)));
            // holder.iv_status2.setImageResource(R.drawable.grey_circle)
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
        val staus1 = view!!.tv_status1
        val staus2 = view!!.tv_status2
        val iv_status1 = view!!.iv_status1
        val iv_status2 = view!!.iv_status2
        val tv_state1 = view!!.tv_state1
        val tv_state2 = view!!.tv_state2

        init {
            view!!.setOnClickListener(View.OnClickListener {
                listener.onLawClick(laws.get(adapterPosition))
            })
        }
    }
}