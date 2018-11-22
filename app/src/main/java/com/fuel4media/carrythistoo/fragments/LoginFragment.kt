package com.fuel4media.carrythistoo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.LoginActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.request.LoginRequest
import com.fuel4media.carrythistoo.model.response.LoginResponse
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.requester.LoginRequester
import com.fuel4media.carrythistoo.requester.UpdateFirebaseTokenRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe

class LoginFragment : BaseFragment() {

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.LOGIN_SUCCESS -> {
                    //startActivity(Intent(activity, DashboaredActivity::class.java))
                    (activity as LoginActivity).replaceFragmentWithTag(VerificationCodeFragment.newInstance(edt_mobile_no.text!!.trim().toString(), (eventObject.`object` as LoginResponse).verification_code!!, -1), VerificationCodeFragment.TAG)
                }
                EventConstant.LOGIN_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppPreference.getInstance().latitude == 0.0) {
            val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, false)
            val location = locationManager.getLastKnownLocation(provider)

            if (location != null) {
                AppPreference.getInstance().latitude = location.latitude
                AppPreference.getInstance().longitude = location.longitude
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_register.setOnClickListener {
            (activity as LoginActivity).replaceFragment(RegisterFragment())
        }

        btn_send_verify_code.setOnClickListener {
            if (valid()) {
                Utility.showProgressBarSmall(rl_progress_bar)
                val loginRequest = LoginRequest()
                loginRequest.mobile = edt_mobile_no.text.toString()
                BackgroundExecutor().execute(LoginRequester(loginRequest))
                // (activity as LoginActivity).replaceFragmentWithTag(VerificationCodeFragment.newInstance(edt_mobile_no.text.trim().toString()), VerificationCodeFragment.TAG)
            }
        }
    }

    private fun valid(): Boolean {
        if (edt_mobile_no.text!!.isEmpty()) {
            Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_mobile_no.text!!.trim().length != 10) {
            Toast.makeText(context, "Mobile number must be 10 character", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }
}
