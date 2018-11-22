package com.fuel4media.carrythistoo.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.PopupWindow
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.fragments.AddPermitFragment
import com.fuel4media.carrythistoo.fragments.SettingsFragment
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.model.PermitType
import com.fuel4media.carrythistoo.model.State
import kotlinx.android.synthetic.main.dialog_add_new_event.*
import kotlinx.android.synthetic.main.dialog_add_suggestion.*
import kotlinx.android.synthetic.main.dialog_edit_delete_event.*
import kotlinx.android.synthetic.main.dialog_manage_subscription.*
import kotlinx.android.synthetic.main.dialog_open_gallary.*
import kotlinx.android.synthetic.main.dialog_payment_success.*
import kotlinx.android.synthetic.main.dialog_premium.*
import kotlinx.android.synthetic.main.law_description.*
import java.util.*


object DialogUtil {
    private val TAG = DialogUtil::class.java.name
    var mDialog: Dialog? = null
    var mWindow: PopupWindow? = null

    interface AlertDialogInterface {
        interface TwoButtonDialogClickListener {
            fun onPositiveButtonClick()

            fun onNegativeButtonClick()
        }

        interface OneButtonDialogClickListener {
            fun onButtonClick()
        }

        interface StateDialogClickListener {
            fun onStateSelected(stateId: Int)
        }

        interface EstablistTypeDialogClickListener {
            fun onEstablishmentTypeSelected(type: String)
        }

        interface EditDeleteDialogClickListener {
            fun onEditClick()
            fun onDeleteClick()
        }

        interface PremiumDialogClickListener {
            fun onSkipClick()
            fun onGoToSettingsClick()
        }

        interface ManageSubscriptionDialogListener {
            fun onPaymentUpdateInfoClick()
            fun onCaneclPremiumClick()
            fun onGoPremiumClick()
            fun onStartFreeTrialClick()

        }

        interface AddEventDialogClickListener {
            fun onAddEvent(event: Event)
        }

        interface OnCompletionDialogClickListener {
            fun onOkButtonClick()
        }


        interface OnAddSuggestionClickListener {
            fun onSendClick(string: String)
        }

        interface OpenCameraDialogListener {
            fun onCameraClick()

            fun onGalleryClick()
        }
    }

    fun showStateListing(context: Context, stateList: ArrayList<State>, listener: AlertDialogInterface.StateDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your state")
        builder.setCancelable(false)
        var selectedState = 1;

// add a radio button list
        val states: Array<String?> = stateList.map { it.state_name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).state_id!!.toInt()
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onStateSelected(selectedState)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showEstablishmentType(context: Context, listener: AlertDialogInterface.EstablistTypeDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Establishment Type")
        builder.setCancelable(false)


// add a radio button list
        val states: Array<String?> = context.resources.getStringArray(R.array.arr_establishment_type);

        var selectedState = states.get(0)
        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = states.get(p1)
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onEstablishmentTypeSelected(selectedState!!)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showStateListingRegister(context: Context, stateList: ArrayList<State>, listener: AlertDialogInterface.StateDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your state")
        builder.setCancelable(false)
        var selectedState = 1;

// add a radio button list
        val states: Array<String?> = stateList.map { it.state_name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).state_id!!.toInt()
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onStateSelected(selectedState)
        }

// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showPermits(context: Context, stateList: ArrayList<PermitType>, listener: AlertDialogInterface.StateDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your permit")
        builder.setCancelable(false)
        var selectedState = 1;

// add a radio button list
        val states: Array<String?> = stateList.map { it.permit_name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).permit_id!!.toInt()
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onStateSelected(selectedState)
        }

// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showMultipleSelectState(context: Context, stateList: ArrayList<State>, listener: AlertDialogInterface.StateDialogClickListener) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select State")

// add a checkbox list
        val states = stateList.map { it.state_name }.toTypedArray();

        val checkedItems = stateList.map { it.status }.toTypedArray();

        /* builder.setMultiChoiceItems(states, checkedItems) { dialog, which, isChecked ->
             // user checked or unchecked a box
         }*/
        /*  builder.setAdapter(StateAdapter(context, stateList, object : StateAdapter.OnClickListener {
              override fun onCheckBoxClick(value: Boolean, contact: State) {

              }
          }))
  */
// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            // user clicked OK
        }
        builder.setNegativeButton("Cancel", null)

// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }


    fun showAddEventDialog(context: Context?, event: Event?, listener: AlertDialogInterface.AddEventDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_add_new_event)
        mDialog!!.setCancelable(false)

        val title = mDialog!!.tv_title
        val btnAddEvent = mDialog!!.btn_add_event
        val ivClose = mDialog!!.iv_close_event
        val edtEventName = mDialog!!.edt_event_name
        val edtEventDescription = mDialog!!.edt_event_description
        val edtEventDate = mDialog!!.edt_event_date
        val edtEventTime = mDialog!!.edt_event_time
        val edtEventLocation = mDialog!!.edt_event_location
        val cbNotify = mDialog!!.cb_notify

        val newEvent = Event()

        if (event != null) {
            newEvent.id = event.id
            title.text = "Edit Event"
            btnAddEvent.setText("Edit")
            edtEventName.setText(event.event_name)
            edtEventDescription.setText(event.descriptions)
            edtEventDate.text = event.date
            edtEventTime.text = event.time
            edtEventLocation.text = event.addlocation
            cbNotify.isChecked = Utility.convertIntToBoolean(event.notify!!)
        }

        ivClose.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
        })

        edtEventDate.setOnClickListener(View.OnClickListener {
            // Get Current Date
            /* if (event != null) {
                 Toast.makeText(context, "Can't edit event date", Toast.LENGTH_SHORT).show()
             } else {*/
            val c = Calendar.getInstance();
            val mYear = c.get(Calendar.YEAR);
            val mMonth = c.get(Calendar.MONTH);
            val mDay = c.get(Calendar.DAY_OF_MONTH);
            val datePickerDialog = DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> edtEventDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay)
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show()
            //}
        })

        edtEventTime.setOnClickListener(View.OnClickListener {
            // Get Current Time
            /*  if (event != null) {
                  Toast.makeText(context, "Can't edit event time", Toast.LENGTH_SHORT).show()
              } else {*/
            val c = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(context,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> edtEventTime.setText(hourOfDay.toString() + ":" + minute) }, mHour, mMinute, false)
            timePickerDialog.show()
            //}
        })

        edtEventLocation.setOnClickListener(View.OnClickListener {
            /* val placeSearchDialog = PlaceSearchDialog.Builder(context)
                     .setLocationNameListener {
                         edtEventLocation.setText(it)
                     }
                     .build()
             placeSearchDialog.show()*/


        })

        btnAddEvent.setOnClickListener(View.OnClickListener {

            if (TextUtils.isEmpty(edtEventName.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Event name is empty ")
            } else if (TextUtils.isEmpty(edtEventDescription.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Event description is empty ")
            } else if (TextUtils.isEmpty(edtEventDate.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Event date is empty ")
            } else if (TextUtils.isEmpty(edtEventTime.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Event time is empty ")
            } else if (TextUtils.isEmpty(edtEventLocation.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Event location is empty ")
            } else {
                newEvent.event_name = edtEventName.text.toString()
                newEvent.descriptions = edtEventDescription.text.toString()
                newEvent.date = edtEventDate.text.toString()
                newEvent.time = edtEventTime.text.toString()
                newEvent.addlocation = edtEventLocation.text.toString()
                newEvent.notify = Utility.convertBooleanToInt(cbNotify.isChecked)

                mDialog!!.dismiss()

                listener.onAddEvent(newEvent)
            }

            //listener.onOkButtonClick()
        })

        mDialog!!.show()

    }

    fun showEditDeleteEventDialog(context: Context, listener: AlertDialogInterface.EditDeleteDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_edit_delete_event)
        mDialog!!.setCancelable(true)

        val btnEdit = mDialog!!.ll_edit
        val btnDelete = mDialog!!.ll_delete

        btnEdit.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onEditClick()
        })

        btnDelete.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onDeleteClick()
        })

        mDialog!!.show()
    }

    fun showAddSuggestionDialog(context: Context, listener: AlertDialogInterface.OnAddSuggestionClickListener) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_add_suggestion)
        mDialog!!.setCancelable(false)

        val btnSend = mDialog!!.btn_send_suggestion
        val btnClose = mDialog!!.iv_close
        val edt_message = mDialog!!.edt_message

        btnClose.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
        })

        btnSend.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(edt_message.text.toString())) {
                CommonMethods.showShortToast(context, "Please enter message")
            } else {
                mDialog!!.dismiss()
                listener.onSendClick(edt_message.text.toString())
            }
        })

        mDialog!!.show()
    }

    fun showPaymentSuccesDialog(context: Context, listener: AlertDialogInterface.OneButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_payment_success)
        mDialog!!.setCancelable(false)

        val btnDone = mDialog!!.tv_done


        btnDone.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onButtonClick()
        })

        mDialog!!.show()
    }

    fun openChooseMediaDialog(context: Context, cameraListener: AlertDialogInterface.OpenCameraDialogListener) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_open_gallary)
        val v = dialog.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
        val camLayout = dialog.cam_layout
        val galLayout = dialog.gal_layout

        camLayout.setOnClickListener(View.OnClickListener {
            cameraListener.onCameraClick()
            dialog.dismiss()
        })

        galLayout.setOnClickListener(View.OnClickListener {
            cameraListener.onGalleryClick()
            dialog.dismiss()
        })

        dialog.show()
    }

    /*
    public static void showOkCancelDialog(Context context, String title, String message, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }*/

    /*    public static void showTwoButtonDialog(Context context, String message, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }*/

    fun showTwoButtonDialog(context: Context, title: String, message: String, positiveBtnString: String, negBtnString: String, dialogInterface: AlertDialogInterface.TwoButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing) {
            return
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onPositiveButtonClick()
                }
                .setNegativeButton(negBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onNegativeButtonClick()
                }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun showTwoButtonDialog(context: Context, message: String, positiveBtnString: String, negBtnString: String, dialogInterface: AlertDialogInterface.TwoButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing) {
            //return;
            dismiss()
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setPositiveButton(positiveBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onPositiveButtonClick()
                }
                .setNegativeButton(negBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onNegativeButtonClick()
                }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun showOneButtonDialog(context: Context, message: String, btnString: String, dialogInterface: AlertDialogInterface.OneButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing) {
            //return;
            dismiss()
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setPositiveButton(btnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onButtonClick()
                }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun showOneButtonDialog(context: Context, message: String, btnString: String) {
        if (mDialog != null && mDialog!!.isShowing) {
            dismiss()
            //return
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message).setPositiveButton(btnString) { dialog, id ->
            mDialog!!.dismiss()
        }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun showOneButtonDialog(context: Context, title: String, message: String, btnString: String, dialogInterface: AlertDialogInterface.OneButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing) {
            dismiss()
            //return
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(btnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onButtonClick()
                }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun dismiss() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    fun showToastShortLength(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLongLength(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showLawDescDialog(contex: Context, description: String?) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(contex)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.law_description)
        mDialog!!.setCancelable(true)

        val btnClose = mDialog!!.iv_close_law
        val edt_message = mDialog!!.tv_desc

        edt_message.setText(Html.fromHtml(description))

        btnClose.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
        })

        mDialog!!.show()
    }

    fun showPremiumDailog(context: Context?) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_premium)
        mDialog!!.setCancelable(false)

        val btnGoToSettings = mDialog!!.tv_go_to_settings
        val btnSkip = mDialog!!.tv_skip

        btnSkip.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            //listener.onSkipClick()
        })

        btnGoToSettings.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            //listener.onGoToSettingsClick()
            if (context is DashboaredActivity) {
                (context as DashboaredActivity).replaceFragment(SettingsFragment())
            }
        })

        mDialog!!.show()
    }

    fun showManageSubscriptionDialog(context: Context?, listener: AlertDialogInterface.ManageSubscriptionDialogListener) {
        if (mDialog != null && mDialog!!.isShowing()) {
            // return;
            dismiss()
        }
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.dialog_manage_subscription)
        mDialog!!.setCancelable(true)

        val btn_update_info = mDialog!!.tv_update_payment_info
        val btn_cancel_subscription = mDialog!!.tv_cancel_subscription
        val btn_go_premium = mDialog!!.tv_go_premium

        if (UserManager.getInstance().user.user_type == 4) {
            btn_go_premium.visibility = View.VISIBLE
            btn_update_info.visibility = View.VISIBLE
        }

        if (UserManager.getInstance().user.user_type == 2 || UserManager.getInstance().user.user_type == 3) {
            btn_update_info.visibility = View.VISIBLE
            btn_cancel_subscription.visibility = View.VISIBLE
        }

        btn_update_info.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onPaymentUpdateInfoClick()
        })

        btn_cancel_subscription.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onCaneclPremiumClick()
        })

        btn_go_premium.setOnClickListener(View.OnClickListener {
            mDialog!!.dismiss()
            listener.onGoPremiumClick()
        })

        /*  btn_start_free_trial.setOnClickListener(View.OnClickListener {
              mDialog!!.dismiss()
              listener.onStartFreeTrialClick()
          })*/

        mDialog!!.show()

    }


}
