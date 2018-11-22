package com.fuel4media.carrythistoo.fragments


import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.CarryThisTooApplication
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Settings
import com.fuel4media.carrythistoo.requester.CancelPaymentRequester
import com.fuel4media.carrythistoo.requester.StartPremiumPlanRequester
import com.fuel4media.carrythistoo.requester.UpdateSettingsRequester
import com.fuel4media.carrythistoo.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment() {

    companion object {
        var TAG: String = SettingsFragment::class.java.simpleName
    }

    var isCancelPremium = false

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.UPDATE_SETTINGS_SUCCESS -> {

                }
                EventConstant.UPDATE_SETTINGS_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    initView()
                }

                EventConstant.CANCEL_PAYMENT_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    initView()
                }

                EventConstant.CANCEL_PAYMENT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.START_PREMIUM_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    initView()
                }

                EventConstant.START_PREMIUM_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_settings), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        switch_push_notification.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.push_notification = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_calendar_notification.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.calender_notification = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_calendar_sync.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.calender_sync = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_grant_access.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.grant_access_app_line = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_sms_notification.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.sms_notification = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_turn_on_location.setOnCheckedChangeListener { compoundButton, b ->
            val settings = Settings()
            settings.turn_on_location = Utility.convertBooleanToInt(b)
            updateSettings(settings)
        }

        switch_cancel_premium.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                DialogUtil.showTwoButtonDialog(context!!, "Are you sure , you want to Cancel Premium Subscription?", getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        val settings = Settings()
                        settings.cancel_premium = Utility.convertBooleanToInt(b)
                        updateSettings(settings)
                    }

                    override fun onNegativeButtonClick() {
                        initView()
                    }
                })
            } else {
                val settings = Settings()
                settings.cancel_premium = Utility.convertBooleanToInt(b)
                updateSettings(settings)
            }
        }
    }

    private fun initView() {
        val currentSettings = UserManager.getInstance().settings

        val userType = UserManager.getInstance().user.user_type;
        if (UserManager.getInstance().isFreeium || userType == 4) {
            tv_subscription_type.text = "Freemium"
        } else if (userType == 2) {
            tv_subscription_type.text = "Free-Trial"
        } else if (userType == 3) {
            tv_subscription_type.text = "Premium"
        }

        tv_pin.setText(String.format(getString(R.string.tv_pin), UserManager.getInstance().user.pin.toString()))

        tv_manage_subscription.setOnClickListener(View.OnClickListener {
            if (UserManager.getInstance().isFreeium) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else {
                DialogUtil.showManageSubscriptionDialog(context, object : DialogUtil.AlertDialogInterface.ManageSubscriptionDialogListener {
                    override fun onPaymentUpdateInfoClick() {


                    }

                    override fun onGoPremiumClick() {
                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().getInstance().execute(StartPremiumPlanRequester())
                    }

                    override fun onStartFreeTrialClick() {

                    }

                    override fun onCaneclPremiumClick() {
                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().getInstance().execute(CancelPaymentRequester())
                    }
                })
            }
        })

        switch_turn_on_location.isChecked = Utility.convertIntToBoolean(currentSettings.turn_on_location!!)
        switch_sms_notification.isChecked = Utility.convertIntToBoolean(currentSettings.sms_notification!!)
        switch_grant_access.isChecked = Utility.convertIntToBoolean(currentSettings.grant_access_app_line!!)
        switch_calendar_sync.isChecked = Utility.convertIntToBoolean(currentSettings.calender_sync!!)
        switch_calendar_notification.isChecked = Utility.convertIntToBoolean(currentSettings.calender_notification!!)
        switch_push_notification.isChecked = Utility.convertIntToBoolean(currentSettings.push_notification!!)
        // switch_cancel_premium.isChecked = Utility.convertIntToBoolean(currentSettings.cancel_premium!!)

    }

    private fun updateSettings(settings: Settings) {
        if (!ConnectivityController.isNetworkAvailable(CarryThisTooApplication.applicationContext())) {
            EventBus.getDefault().post(EventObject(EventConstant.NETWORK_ERROR, ""))
            initView()
            return
        }
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(UpdateSettingsRequester(settings))
    }

}// Required empty public constructor
