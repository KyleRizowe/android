package com.fuel4media.carrythistoo.fragments


import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.adapter.ChatAdapter
import com.fuel4media.carrythistoo.enums.MessageType
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.Message
import com.fuel4media.carrythistoo.requester.GetMessagesRequester
import com.fuel4media.carrythistoo.requester.SendMessageRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_search_gun_zone.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment() {

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.MESSAGE_LIST_SUCCESS -> {
                    messages.clear()

                    if (eventObject.`object` != null) {
                        messages.addAll(eventObject.`object` as ArrayList<Message>)
                    }

                    adapter!!.notifyDataSetChanged()

                }
                EventConstant.MESSAGE_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })

    }

    val messages: ArrayList<Message> = ArrayList()
    var adapter: ChatAdapter? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_message), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(GetMessagesRequester())



        adapter = ChatAdapter(activity as Context, messages)

        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        rv_messages.layoutManager = layoutManager

        rv_messages.adapter = adapter

        tv_send_message.setOnClickListener(View.OnClickListener {
            if (!ed_message.text.isEmpty()) {
                val message1 = Message()
                message1.messages = ed_message.text.toString()
                message1.message_type = MessageType.OUTGOING.type
                message1.timestamp = System.currentTimeMillis() / 1000
                messages.add(message1)
                adapter!!.notifyDataSetChanged()
                rv_messages.smoothScrollToPosition(this.messages.size)
                ed_message.setText("")
                BackgroundExecutor().execute(SendMessageRequester(message1))
            }
        })
    }

    fun messageNotification(message: Message?) {
        messages.add(message!!)
        adapter!!.notifyDataSetChanged()
        rv_messages.smoothScrollToPosition(this.messages.size)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utility.hideKeyboardFrom(context!!, ed_message)
    }
}// Required empty public constructor
