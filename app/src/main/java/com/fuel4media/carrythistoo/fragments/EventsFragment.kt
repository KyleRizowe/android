package com.fuel4media.carrythistoo.fragments


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.AddEventActivity
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.EventListAdapter
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.model.request.CalendarRequest
import com.fuel4media.carrythistoo.model.response.EventList
import com.fuel4media.carrythistoo.requester.DeleteEventRequester
import com.fuel4media.carrythistoo.requester.EditEventRequester
import com.fuel4media.carrythistoo.requester.EventListRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.greenrobot.eventbus.Subscribe
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class EventsFragment : BaseFragment(), EventListAdapter.EventClickListener {
    override fun onEventClick(index: Int, event: Event) {
        if (!isHistoryDate()) {
            DialogUtil.showEditDeleteEventDialog(context!!, object : DialogUtil.AlertDialogInterface.EditDeleteDialogClickListener {
                override fun onEditClick() {
                    /* DialogUtil.showAddEventDialog(context, event, object : DialogUtil.AlertDialogInterface.AddEventDialogClickListener {
                         override fun onAddEvent(event: Event) {
                             editEvent = event
                             editIndex = index
                             editEventToServer(event)
                         }
                     })*/

                    if (UserManager.getInstance().isFreeium()) {
                        startActivity(Intent(context, PaymentActivity::class.java))
                    } else if (UserManager.getInstance().isOnTrial()) {
                        DialogUtil.showPremiumDailog(context)
                    } else {
                        startActivity(AddEventActivity.newInstance(context!!, event))
                    }

                }

                override fun onDeleteClick() {
                    // Toast.makeText(context, "on Delete Click ", Toast.LENGTH_SHORT).show()
                    if (UserManager.getInstance().isFreeium()) {
                        startActivity(Intent(context, PaymentActivity::class.java))
                    } else if (UserManager.getInstance().isOnTrial()) {
                        DialogUtil.showPremiumDailog(context)
                    } else {
                        var msg1: String? = null
                        val msg = getString(R.string.msg_delete_event, event.event_name)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            msg1 = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY).toString()
                        } else {
                            msg1 = Html.fromHtml(msg).toString()
                        }

                        DialogUtil.showTwoButtonDialog(context!!, msg1, "Yes", "No", object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                            override fun onPositiveButtonClick() {
                                deleteEvent = event
                                deleteEventToServer(event)
                            }

                            override fun onNegativeButtonClick() {

                            }

                        })
                    }
                }
            })
        } else {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                var msg2: String? = null
                val msg = getString(R.string.msg_delete_event, event.event_name)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    msg2 = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    msg2 = Html.fromHtml(msg).toString()
                }

                DialogUtil.showTwoButtonDialog(context!!, msg2, "Yes", "No", object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        deleteEvent = event
                        deleteEventToServer(event)
                    }

                    override fun onNegativeButtonClick() {

                    }

                })
            }
        }
    }

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    fun editEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(EditEventRequester(event))
    }

    fun deleteEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(DeleteEventRequester(event.id!!))
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_EVENT_SUCCESS -> {
                    /*events.add(this.addEvent!!)
                    eventsAdapter!!.notifyDataSetChanged()*/

                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(EventListRequester(CalendarRequest(tv_date.text.toString())))

                    //CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.ADD_EVENT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.EDIT_EVENT_SUCCESS -> {
                    /* events.set(this.editIndex!!, this.editEvent!!)
                     eventsAdapter!!.notifyDataSetChanged()*/

                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(EventListRequester(CalendarRequest(tv_date.text.toString())))

                    //CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.EDIT_EVENT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.DELETE_EVENT_SUCCESS -> {
                    events.remove(deleteEvent)
                    eventsAdapter!!.notifyDataSetChanged()

                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
                EventConstant.DELETE_EVENT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.EVENT_LIST_SUCCESS -> {
                    val eventList: EventList = eventObject.`object` as EventList

                    events.clear()
                    events.addAll(eventList.events as ArrayList<Event>)
                    eventsAdapter!!.notifyDataSetChanged()
                }

                EventConstant.EVENT_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    var events = ArrayList<Event>()
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var eventsAdapter: EventListAdapter? = null
    var deleteEvent: Event? = null
    var addEvent: Event? = null
    var editEvent: Event? = null
    var editIndex: Int? = null

    companion

    object {
        var TAG: String = EventsFragment::class.java.simpleName
        var KEY_YEAR: String = "year"
        var KEY_MONTH: String = "month"
        var KEY_DAY: String = "day"

        fun newInstance(year: Int, month: Int,
                        dayOfMonth: Int): EventsFragment {
            val fragment = EventsFragment()
            val args = Bundle()
            args.putInt(KEY_YEAR, year)
            args.putInt(KEY_MONTH, month)
            args.putInt(KEY_DAY, dayOfMonth)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_events), true, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        year = args!!.getInt(EventsFragment.KEY_YEAR)
        month = args!!.getInt(EventsFragment.KEY_MONTH)
        day = args!!.getInt(EventsFragment.KEY_DAY)

        updateDate()

        iv_calendar.setOnClickListener(View.OnClickListener {
            // Get Current Date
            val datePickerDialog = DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        this.year = year
                        this.month = monthOfYear
                        this.day = dayOfMonth
                        updateDate()
                        enablePlusIcon()
                    }, year!!, month!!, day!!)
            datePickerDialog.show()
        })


        enablePlusIcon()

        (activity as DashboaredActivity).getToolbar()!!.iv_plus.setOnClickListener(View.OnClickListener {
            /* DialogUtil.showAddEventDialog(context, null, object : DialogUtil.AlertDialogInterface.AddEventDialogClickListener {
                 override fun onAddEvent(event: Event) {
                     addEvent = event
                     Utility.showProgressBarSmall(rl_progress_bar)
                     BackgroundExecutor().getInstance().execute(AddEventRequester(event))


                 }
             })*/
            startActivity(AddEventActivity.newInstanceDate(context!!, tv_date.text.toString()))
        })


        eventsAdapter = EventListAdapter(activity as Context, events, this)
        rv_events.layoutManager = LinearLayoutManager(context)
        rv_events.adapter = eventsAdapter
    }

    private fun updateDate() {
        tv_date.text = "" + (month!!.plus(1)) + "-" + day + "-" + year

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(EventListRequester(CalendarRequest("" + day + "-" + (month!!.plus(1)) + "-" + year)))
    }


    fun enablePlusIcon() {
        if (isHistoryDate()) {
            (activity as DashboaredActivity).getToolbar()!!.iv_plus.visibility = View.GONE
        } else {
            (activity as DashboaredActivity).getToolbar()!!.iv_plus.visibility = View.VISIBLE
        }
    }

    private fun isHistoryDate(): Boolean {

        try {
            val sdf = SimpleDateFormat("MM-dd-yyyy")
            val date1 = sdf.parse(tv_date.text.toString());

            val currentDate = sdf.parse(sdf.format(Date()))

            return date1.before(currentDate) && !date1.equals(currentDate)

        } catch (ex: ParseException) {
            ex.printStackTrace();
        }

        return false
    }
//}
}// Required empty public constructor



