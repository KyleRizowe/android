package com.fuel4media.carrythistoo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.requester.AddEventRequester
import com.fuel4media.carrythistoo.requester.EditEventRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.Utility
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.R.id.text1
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension


class AddEventActivity : BaseActivity() {

    val PLACE_PICKER_REQUEST = 1
    var edtEventLocation: TextView? = null
    var dateTimestamp: Long = 0

    companion
    object {
        var TAG: String = AddEventActivity::class.java.simpleName
        var KEY_EVENT: String = "event"
        var KEY_DATE: String = "date"

        fun newInstance(context: Context, event: Event?): Intent {
            val intent = Intent(context, AddEventActivity::class.java)
            intent.putExtra(KEY_EVENT, event)
            return intent
        }

        fun newInstanceDate(context: Context, date: String?): Intent {
            val intent = Intent(context, AddEventActivity::class.java)
            intent.putExtra(KEY_DATE, date)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        var event: Event? = null
        var date: String? = null

        if (getIntent().hasExtra(KEY_EVENT)) {
            event = getIntent().getParcelableExtra(KEY_EVENT)

        }

        if (getIntent().hasExtra(KEY_DATE)) {
            date = getIntent().getStringExtra(KEY_DATE)
        }

        val newEvent = Event()

        val title = tv_title
        val btnAddEvent = btn_add_event
        val ivClose = iv_close_event
        val edtEventName = edt_event_name
        val edtEventDescription = edt_event_description
        val edtEventDate = edt_event_date
        //  val edtEventTime = edt_event_time
        edtEventLocation = edt_event_location
        val cbNotify = cb_notify

        if (event != null) {
            newEvent.id = event.id
            dateTimestamp = event.timestamp * 1000
            title.text = "Edit Event"
            btnAddEvent.setText("Save")
            edtEventName.setText(event.event_name)
            edtEventDescription.setText(event.descriptions)
            edtEventDate.text = CommonMethods.changeDateFormat(event.timestamp)
            //edtEventTime.text = event.time
            edtEventLocation!!.text = event.addlocation
            cbNotify.isChecked = Utility.convertIntToBoolean(event.notify!!)
        }

        ivClose.setOnClickListener(View.OnClickListener {
            finish()
        })

        edtEventDate.setOnClickListener(View.OnClickListener {
            // Get Current Date
            /* if (event != null) {
                 Toast.makeText(context, "Can't edit event date", Toast.LENGTH_SHORT).show()
             } else {*/

            val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance("",
                    "OK",
                    "Cancel"
            )

            dateTimeDialogFragment.startAtCalendarView()
            dateTimeDialogFragment.set24HoursMode(false)
            //dateTimeDialogFragment.setAlertStyle(R.style.MyAppCustomTheme)
            dateTimeDialogFragment.setDefaultDateTime(Calendar.getInstance().time)
            //dateTimeDialogFragment.minimumDateTime = Calendar.getInstance().time

            dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");

            dateTimeDialogFragment.setOnButtonClickListener(object : SwitchDateTimeDialogFragment.OnButtonClickListener {
                override fun onPositiveButtonClick(date: Date) {
                    // Date is get on positive button click
                    // Do something
                    Log.d(TAG, "" + date.time)
                    dateTimestamp = date.time
                    edtEventDate.text = CommonMethods.changeDateFormat(date)
                }

                override fun onNegativeButtonClick(date: Date) {
                    // Date is get on negative button click
                }
            })

            /* SingleDateAndTimePickerDialog.Builder(this)
                     .bottomSheet()
                     .mainColor(resources.getColor(R.color.colorAccent))
                     .displayYears(true)
                     .displayMonth(true)
                     .displayDaysOfMonth(true)
                     .displayDays(false)
                     .displayMonthNumbers(true)
                     .minutesStep(1)
                     .mustBeOnFuture()

                     .displayListener {
                         //retrieve the SingleDateAndTimePicker
                         if (event != null) {
                             it.setDefaultDate(Date(event!!.timestamp * 1000))
                         } else {
                             if (date != null) {
                                 val format = SimpleDateFormat("MM-dd-yyyy")
                                 it.setDefaultDate(format.parse(date))
                             } else {
                                 it.setDefaultDate(Date())
                             }
                         }
                     }

                     .listener(object : SingleDateAndTimePickerDialog.Listener {
                         override fun onDateSelected(date: Date) {
                             Log.d(TAG, "" + date.time)
                             dateTimestamp = date.time
                             edtEventDate.text = CommonMethods.changeDateFormat(date)
                         }
                     }).display()
 */

        })

        /*  edtEventTime.setOnClickListener(View.OnClickListener {
              // Get Current Time
              *//*  if (event != null) {
                  Toast.makeText(context, "Can't edit event time", Toast.LENGTH_SHORT).show()
              } else {*//*

            if (TextUtils.isEmpty(edtEventDate.text.toString())) {
                CommonMethods.showShortToast(this!!, "Please select Date")
            } else {

                val c = Calendar.getInstance()
                val mHour = c.get(Calendar.HOUR_OF_DAY)
                val mMinute = c.get(Calendar.MINUTE)

                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->


                            if (isTodayDate(edtEventDate.text.toString())) {
                                val datetime = Calendar.getInstance();
                                val c1 = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                if (datetime.getTimeInMillis() >= c1.timeInMillis) {
                                    edtEventTime.setText(hourOfDay.toString() + ":" + minute)
                                } else {
                                    //it's before current'
                                    Toast.makeText(getApplicationContext(), "Time must be grater than current time.", Toast.LENGTH_LONG).show();
                                    edtEventTime.setText("")
                                }
                            } else {
                                edtEventTime.setText(hourOfDay.toString() + ":" + minute)
                            }

                        }, mHour, mMinute, true)
                timePickerDialog.show()
            }
            //}
        })
*/
        edtEventLocation!!.setOnClickListener(View.OnClickListener {
            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)

        })

        btnAddEvent.setOnClickListener(View.OnClickListener {

            if (TextUtils.isEmpty(edtEventName.text.toString().trim())) {
                CommonMethods.showShortToast(this!!, "Event name is empty ")
            } else if (TextUtils.isEmpty(edtEventDescription.text.toString().trim())) {
                CommonMethods.showShortToast(this!!, "Event description is empty ")
            } else if (TextUtils.isEmpty(edtEventDate.text.toString().trim())) {
                CommonMethods.showShortToast(this!!, "Event date is empty ")
            }/* else if (TextUtils.isEmpty(edtEventTime.text.toString().trim())) {
                CommonMethods.showShortToast(this!!, "Event time is empty ")*/
            else if (TextUtils.isEmpty(edtEventLocation!!.text.toString().trim())) {
                CommonMethods.showShortToast(this!!, "Event location is empty ")
            } else if (dateTimestamp <= System.currentTimeMillis()) {
                CommonMethods.showShortToast(this!!, "Please enter future time and date ")
            } else {
                newEvent.event_name = edtEventName.text.toString()
                newEvent.descriptions = edtEventDescription.text.toString()
                newEvent.timestamp = dateTimestamp / 1000
                //newEvent.time = edtEventTime.text.toString()
                newEvent.addlocation = edtEventLocation!!.text.toString()
                newEvent.notify = Utility.convertBooleanToInt(cbNotify.isChecked)

                // listener.onAddEvent(newEvent)

                if (event != null) {
                    editEventToServer(newEvent)
                } else {
                    addEventToServer(newEvent)
                }
            }


            //listener.onOkButtonClick()
        })

    }

    private fun isTodayDate(toString: String): Boolean {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date1 = sdf.parse(toString)

        return date1.equals(sdf.parse(sdf.format(Date())))
    }


    fun editEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(EditEventRequester(event))
    }

    fun addEventToServer(event: Event) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(AddEventRequester(event))
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_EVENT_SUCCESS -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                    finish()

                }
                EventConstant.ADD_EVENT_ERROR -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                }

                EventConstant.EDIT_EVENT_SUCCESS -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                    finish()

                }
                EventConstant.EDIT_EVENT_ERROR -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = PlacePicker.getPlace(data, this);

                val text1 = place.name

                val finalText = TextUtils.concat(text1, " \n", place.address)

                val span1 = SpannableString(finalText)
                span1.setSpan(RelativeSizeSpan(1.3f), 0, text1.length, 0);

                edtEventLocation!!.text = span1
            }
        }
    }

    fun spToPx(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
    }
}
