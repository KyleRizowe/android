package com.fuel4media.carrythistoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.enums.MessageType
import com.fuel4media.carrythistoo.model.Message

/**
 * Created by shweta on 30/5/18.
 */
class ChatAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView: View? = null
        if (viewType == VIEW_TYPE_INCOMING_MESSAGE) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_imcoming_message, parent, false)

            return IncomingMessageViewHolder(itemView)

        } else {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_outgoing_message, parent, false)

            return OutgoingMessageViewHolder(itemView)

        }
        //return RecyclerView.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = this.messages[position]
        if (holder is IncomingMessageViewHolder) {
            holder.tvIncomingMsg!!.text = message.messages

        } else if (holder is OutgoingMessageViewHolder) {
            holder.tvOutgoingMsg!!.text = message.messages

        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = this.messages[position]
        if (message.message_type!!.equals(MessageType.INCOMING.type)) {
            return VIEW_TYPE_INCOMING_MESSAGE

        } else if (message.message_type.equals(MessageType.OUTGOING.type)) {
            return VIEW_TYPE_OUTGOING_MESSAGE
        }
        return 0
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    internal inner class IncomingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvIncomingMsg: TextView? = null

        init {
            tvIncomingMsg = view!!.findViewById(R.id.tv_incoming_message)
        }

    }

    internal inner class OutgoingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvOutgoingMsg: TextView? = null

        init {
            tvOutgoingMsg = view!!.findViewById(R.id.tv_outgoing_message)
        }
    }

    companion object {

        private val VIEW_TYPE_INCOMING_MESSAGE = 1
        private val VIEW_TYPE_OUTGOING_MESSAGE = 2
    }
}