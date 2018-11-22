package com.fuel4media.carrythistoo.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.LocationActivity
import com.fuel4media.carrythistoo.activity.LoginActivity
import com.fuel4media.carrythistoo.activity.SMSListener
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.OTPListener
import com.fuel4media.carrythistoo.model.request.LoginRequest
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.requester.UpdateFirebaseTokenRequester
import com.fuel4media.carrythistoo.requester.VerifyOtpRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.Utility
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_verification_code.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class VerificationCodeFragment : BaseFragment() {

    var smsListener: SMSListener? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.VERIFY_OTP_SUCCESS -> {
                    startActivity(Intent(activity, DashboaredActivity::class.java))
                    if(AppPreference.getInstance().firebaseToken == null){
                       var token = FirebaseInstanceId.getInstance().getToken()
                        AppPreference.getInstance().firebaseToken = token
                    }
                    BackgroundExecutor().getInstance().execute(UpdateFirebaseTokenRequester(Utility.getDeviceId(), AppPreference.getInstance().firebaseToken))
                }
                EventConstant.VERIFY_OTP_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })


    }

    override fun updateLocationCallback(lastLocation: Location) {
        // location = lastLocation
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    companion object {
        var TAG: String = VerificationCodeFragment::class.java.simpleName
        var KEY_MOBILE: String = "MOBILE"
        var KEY_OTP: String = "OTP"
        var KEY_STATE_ID: String = "state_id"

        fun newInstance(mobileNumber: String, OTP: String, selectedState: Int): VerificationCodeFragment {
            val fragment = VerificationCodeFragment()
            val args = Bundle()
            args.putString(KEY_MOBILE, mobileNumber)
            args.putString(KEY_OTP, OTP)
            args.putInt(KEY_STATE_ID, selectedState)
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SMSListener.unbindListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification_code, container, false)
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val mobileNumber = args!!.getString(KEY_MOBILE)
        val OTP = args!!.getString(KEY_OTP)
        val stateID = args!!.getInt(KEY_STATE_ID)

        SMSListener.bindListener(OTPListener {
            edt_verification_code.setText(it)
        })

        tv_mobile_no.text = "+1 " + mobileNumber

        btn_verify.setOnClickListener(View.OnClickListener {
            if (valid())
                if (OTP.equals(edt_verification_code.text!!.trim().toString())) {
                    Utility.showProgressBarSmall(rl_progress_bar)

                    val verifyOTPRequest = LoginRequest()

                    if (AppPreference.getInstance().latitude == 0.0) {
                        val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
                        val criteria = Criteria()
                        val provider = locationManager.getBestProvider(criteria, false)
                        val location = locationManager.getLastKnownLocation(provider)

                        if (location != null) {

                            AppPreference.getInstance().latitude = location.latitude
                            AppPreference.getInstance().longitude = location.longitude

                            verifyOTPRequest.lat = location.latitude
                            verifyOTPRequest.long = location.longitude
                        }
                    } else {
                        verifyOTPRequest.lat = AppPreference.getInstance().latitude
                        verifyOTPRequest.long = AppPreference.getInstance().longitude
                    }

                    verifyOTPRequest.mobile = mobileNumber
                    verifyOTPRequest.verification_code = edt_verification_code.text.toString()
                    if (stateID != -1) {
                        verifyOTPRequest.state_id = stateID.toString()
                    }
                    BackgroundExecutor().execute(VerifyOtpRequester(verifyOTPRequest))

                    //startActivity(Intent(activity, DashboaredActivity::class.java))
                } else {
                    CommonMethods.showShortToast(activity as Context, "Invalid OTP")
                }
        })
    }

    private fun valid(): Boolean {
        if (edt_verification_code.text!!.isEmpty()) {
            Toast.makeText(context, "Please enter verification code", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }


}
