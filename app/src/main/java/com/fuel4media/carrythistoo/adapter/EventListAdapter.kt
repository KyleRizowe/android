package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.utils.CommonMethods
import kotlinx.android.synthetic.main.row_event_list.view.*

/**
 * Created by shweta on 30/5/18.
 */
class EventListAdapter(val context: Context, val events: ArrayList<Event>, val listener: EventClickListener) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    interface EventClickListener {
        fun onEventClick(index: Int, event: Event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_event_list, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = this.events[position]

        holder.eventName.text = event.event_name
        holder.eventDesc.text = event.descriptions
        holder.eventDate.text = CommonMethods.changeDateFormat(event.timestamp)
        holder.eventAddress.text = event.addlocation

        if (event.notify == 1) {
            holder.ivNotify.setImageDrawable(context.resources.getDrawable(R.drawable.bell_alert_icon))
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName = view!!.tv_event_name
        val eventDesc = view!!.tv_description
        val eventDate = view!!.tv_date
        val eventTime = view!!.tv_time
        val eventAddress = view!!.tv_address
        val ivNotify = view!!.iv_bell

        init {
            view!!.setOnClickListener {
                listener.onEventClick(adapterPosition, events.get(adapterPosition))
            }
        }

    }

}