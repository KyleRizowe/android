package com.fuel4media.carrythistoo.fragments

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.LoginActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.request.LoginRequest
import com.fuel4media.carrythistoo.model.response.LoginResponse
import com.fuel4media.carrythistoo.permissions.PermissionRequest
import com.fuel4media.carrythistoo.permissions.PermissionsUtil
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.requester.RegisterRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


class RegisterFragment : BaseFragment(), PermissionRequest.RequestCalender {

    override fun onCalenderPermissionGranted() {
        Log.d("Calander Sync", "Granted")
        syncCalander()
    }

    override fun onCalenderPermissionDenied() {
        Log.d("Calander Sync", "Denied")
    }

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.REGISTER_SUCCESS -> {
                    val response = eventObject.`object` as LoginResponse
                    DialogUtil.showStateListingRegister(
                            context!!,
                            response.states!!,
                            object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                                override fun onStateSelected(stateId: Int) {
                                    (activity as LoginActivity).replaceFragmentWithTag(VerificationCodeFragment.newInstance(edt_mobile_no.text.trim().toString(), response.verification_code!!, stateId), VerificationCodeFragment.TAG)
                                }
                            }
                    )
                }
                EventConstant.REGISTER_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // PermissionRequestHandler.requestCalender(this, "")

        tv_login.setOnClickListener(View.OnClickListener {
            (activity as LoginActivity).replaceFragment(LoginFragment())
        })

        btn_send_verify.setOnClickListener(View.OnClickListener {
            if (valid()) {
                val registerRequest = LoginRequest()
                registerRequest.mobile = edt_mobile_no.text.toString()
                registerRequest.referral_code = edt_referral_code.text.toString()
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().execute(RegisterRequester(registerRequest))
                // (activity as LoginActivity).replaceFragmentWithTag(VerificationCodeFragment.newInstance(edt_mobile_no.text.trim().toString()), VerificationCodeFragment.TAG)
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun syncCalander() {
        // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
        val EVENT_PROJECTION = arrayOf(Calendars._ID, // 0
                Calendars.ACCOUNT_NAME, // 1
                Calendars.CALENDAR_DISPLAY_NAME, // 2
                Calendars.OWNER_ACCOUNT                  // 3
        )

// The indices for the projection array above.
        val PROJECTION_ID_INDEX = 0
        val PROJECTION_ACCOUNT_NAME_INDEX = 1
        val PROJECTION_DISPLAY_NAME_INDEX = 2
        val PROJECTION_OWNER_ACCOUNT_INDEX = 3

        // Run query
        var cur: Cursor? = null
        val cr = context!!.getContentResolver()
        val uri = Calendars.CONTENT_URI
        val selectionArgs = arrayOf(null, AccountManager.KEY_ACCOUNT_TYPE, null)
// Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null)

        while (cur.moveToNext()) {
            var calID = 0;
            var displayName: String? = null;
            var accountName: String? = null;
            var ownerName: String? = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX).toInt();
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            Log.d("Calendar : ", displayName + "  :" + accountName + " : " + ownerName)
        }
    }

    fun getInstanceCaendar() {


    }

    private fun valid(): Boolean {
        if (edt_mobile_no.text.isEmpty()) {
            Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_mobile_no.text.trim().length != 10) {
            Toast.makeText(context, "Mobile number must be 10 character", Toast.LENGTH_SHORT).show()
            return false
        } /*else if (edt_referral_code.text.isEmpty()) {
            Toast.makeText(context, "Please enter referral code", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_referral_code.text.trim().length != 5) {
            Toast.makeText(context, "Referral code must be 5 character", Toast.LENGTH_SHORT).show()
            return false }*/
        else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults, this)
    }
}
