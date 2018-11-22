package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.utils.CommonMethods
import kotlinx.android.synthetic.main.row_event_item.view.*


/**
 * Created by shweta on 30/5/18.
 */
class EventAdapter(val context: Context, val events: ArrayList<Event>, val isHistory: Boolean, val listener: EventAdapter.CalenderEventClick) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    interface CalenderEventClick {
        fun onEventClick(index: Int, event: Event, isHistory: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_event_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = this.events[position]

        holder.eventDate.text = CommonMethods.changeDateFormatTOMonth(event.timestamp)
        holder.eventDest.text = event.event_name

        if (this.isHistory) {
            holder.itemView.setBackground(context.resources.getDrawable(R.drawable.red_with_solid))
            holder.notify.visibility = View.GONE
        } else {
            holder.eventDate.setTextColor(Color.BLACK);
            holder.eventDest.setTextColor(Color.BLACK);
            ImageViewCompat.setImageTintList(holder.ivCalendar, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTextBlack)));
        }

        if (event.notify == 1) {
            holder.notify.setImageDrawable(context.resources.getDrawable(R.drawable.bell_alert_icon))
            ImageViewCompat.setImageTintList(holder.notify, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
        } else {
            holder.notify.setImageDrawable(context.resources.getDrawable(R.drawable.bell_icon))
        }
    }


    override fun getItemCount(): Int {
        return events.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventDate = view!!.tv_date
        val eventDest = view!!.tv_dest
        val notify = view!!.iv_notify
        val ivCalendar = view!!.iv_calendar

        init {
            view!!.setOnClickListener(View.OnClickListener {
                listener.onEventClick(adapterPosition, events.get(adapterPosition), isHistory)
            })
        }
    }

}