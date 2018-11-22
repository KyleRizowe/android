package com.fuel4media.carrythistoo.fragments


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.AddEventActivity
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.EventAdapter
import com.fuel4media.carrythistoo.customviews.SpacesItemDecoration
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Dates
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.model.request.CalendarRequest
import com.fuel4media.carrythistoo.model.response.CalendarResponse
import com.fuel4media.carrythistoo.requester.CalendarListRequester
import com.fuel4media.carrythistoo.requester.DeleteEventRequester
import com.fuel4media.carrythistoo.requester.EditEventRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.CommonUtil
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.greenrobot.eventbus.Subscribe
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : BaseFragment(), EventAdapter.CalenderEventClick {


    var selectedYear = 0
    var currentYear = 0
    var selectedMonth = 0

    var startYear = 0
    var endYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val c = Calendar.getInstance()

        selectedYear = c.get(Calendar.YEAR)
        currentYear = c.get(Calendar.YEAR)

        selectedMonth = c.get(Calendar.MONTH)

        startYear = selectedYear - 1
        endYear = selectedYear + 1
    }

    override fun onEventClick(index: Int, event: Event, isHistory: Boolean) {
        if (!isHistory) {
            DialogUtil.showEditDeleteEventDialog(context!!, object : DialogUtil.AlertDialogInterface.EditDeleteDialogClickListener {
                override fun onEditClick() {
                    /*DialogUtil.showAddEventDialog(context, event, object : DialogUtil.AlertDialogInterface.AddEventDialogClickListener {
                        override fun onAddEvent(event: Event) {
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
                        deleteEventToServer(event)
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
            }
        }
    }


    fun editEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(EditEventRequester(event))
    }

    fun deleteEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(DeleteEventRequester(event.id!!))
    }

    fun getCalendarList() {
        calendarView.clearSelection()
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(CalendarListRequester(CalendarRequest("01-01-" + startYear, "31-12-" + endYear)))
    }

    var contex: Context? = null

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contex = context
        /* if (context is DashboaredActivity) {
             context.setToolbar(getString(R.string.title_calendar), false, true)
         }*/
    }


    override fun onResume() {
        super.onResume()
        if (contex is DashboaredActivity) {
            (contex as DashboaredActivity).setToolbar(getString(R.string.title_calendar), false, true)
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_EVENT_SUCCESS -> {
                    getCalendarList()
                    // CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
                EventConstant.ADD_EVENT_ERROR -> {
                    // CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.CALENDAR_LIST_SUCCESS -> {
                    val response: CalendarResponse = eventObject.`object` as CalendarResponse

                    eventsUpcoming.clear();
                    eventsUpcoming.addAll(response.upcomings as ArrayList<Event>)
                    adapterUpcoming!!.notifyDataSetChanged()

                    eventsHistory.clear();
                    eventsHistory.addAll(response.history as ArrayList<Event>)
                    adapterHistory!!.notifyDataSetChanged()


                    showDatesOnCalendar(response.dates_list)

                }
                EventConstant.CALENDAR_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.EDIT_EVENT_SUCCESS -> {
                    getCalendarList()
                    // CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.EDIT_EVENT_ERROR -> {
                    //CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.DELETE_EVENT_SUCCESS -> {
                    getCalendarList()
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
                EventConstant.DELETE_EVENT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

            }
        })

    }

    private fun showDatesOnCalendar(dates_list: ArrayList<Dates>?) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        dates_list!!.forEach {

            var date: Date? = null
            try {
                date = sdf.parse(it.dates)

                calendarView.setDateSelected(date, true)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    val eventsHistory = ArrayList<Event>()
    val eventsUpcoming = ArrayList<Event>()
    var adapterUpcoming: EventAdapter? = null
    var adapterHistory: EventAdapter? = null
    val PLACE_PICKER_REQUEST = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place: Place = PlacePicker.getPlace(data, contex);
                val toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCalendarList()

        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE

        (activity as DashboaredActivity).getToolbar()!!.iv_plus.setOnClickListener(View.OnClickListener {

            /*  DialogUtil.showAddEventDialog(context, null, object : DialogUtil.AlertDialogInterface.AddEventDialogClickListener {
                  override fun onAddEvent(event: Event) {


                      val builder = PlacePicker.IntentBuilder()

                      startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)

                      *//* Utility.showProgressBarSmall(rl_progress_bar)
                     BackgroundExecutor().getInstance().execute(AddEventRequester(event))*//*
                }
            })*/

            startActivity(AddEventActivity.newInstance(context!!, null))
        })

        val builder = MonthPickerDialog.Builder(context, object : MonthPickerDialog.OnDateSetListener {
            override fun onDateSet(selectedM: Int, selectedY: Int) {
                selectedYear = selectedY
                selectedMonth = selectedM

                calendarView.setCurrentDate(CalendarDay(selectedY, selectedM, 1))

                if (selectedY < startYear) {
                    startYear = selectedY
                    getCalendarList()
                } else if (selectedY > endYear) {
                    endYear = selectedYear
                    getCalendarList()
                }
            }
        }, selectedYear, selectedMonth)


        calendarView.setOnTitleClickListener(View.OnClickListener {
            builder.setActivatedMonth(selectedMonth).setActivatedYear(selectedYear).setMaxYear(currentYear + 20).setMinYear(currentYear - 20).build().show()
        })


        /* calendarView.setOnMonthChangedListener(object : OnMonthChangedListener {
             override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {

             }
         })
 */

        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                (activity as DashboaredActivity).replaceFragmentWithTag(EventsFragment.newInstance(date.year, date.month, date.day), EventsFragment.TAG)
                calendarView.clearSelection()
            }
        })

        adapterUpcoming = EventAdapter(activity as Context, eventsUpcoming, false, this)

        gv_upcoming_travel.layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?
        gv_upcoming_travel.adapter = adapterUpcoming
        gv_upcoming_travel.addItemDecoration(SpacesItemDecoration(2, CommonUtil.dpToPx(10), false))

        adapterHistory = EventAdapter(activity as Context, eventsHistory, true, this)

        gv_history_travel.layoutManager = GridLayoutManager(context, 2)
        gv_history_travel.adapter = adapterHistory
        gv_history_travel.addItemDecoration(SpacesItemDecoration(2, CommonUtil.dpToPx(10), false))
    }

    fun eventNotification(event: String?) {
        DialogUtil.showOneButtonDialog(context!!, event!!, "OK", object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
            override fun onButtonClick() {

            }
        })
    }


    fun eventNotification(event: Event?) {
        DialogUtil.showOneButtonDialog(context!!, event!!.event_name!!, "OK", object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
            override fun onButtonClick() {

            }
        })
    }

}// Required empty public constructor
