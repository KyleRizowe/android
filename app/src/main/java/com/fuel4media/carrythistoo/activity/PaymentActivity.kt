package com.fuel4media.carrythistoo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fuel4media.carrythistoo.R
import android.content.Intent.getIntent
import android.support.v4.app.ActivityCompat.startActivityForResult
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInActivity
import android.R.attr.data
import android.app.Activity
import android.view.View
import com.braintreepayments.api.dropin.DropInResult
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.model.response.PaymentTokenResponse
import com.fuel4media.carrythistoo.requester.GetClientTokenForPayment
import com.fuel4media.carrythistoo.requester.SendPaymentNonceToServer
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


class PaymentActivity : BaseActivity() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.PAYMENT_TOKEN_SUCCESS -> {
                    val response = eventObject.`object` as PaymentTokenResponse
                    val dropInRequest = DropInRequest()
                            .clientToken(response.payment_token)
                    startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE)
                }
                EventConstant.PAYMENT_TOKEN_ERROR -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                }

                EventConstant.PAYMENT_NONCE_SUCCESS -> {
                    DialogUtil.showPaymentSuccesDialog(this@PaymentActivity, object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
                        override fun onButtonClick() {
                            finish()
                        }
                    })
                }
                EventConstant.PAYMENT_NONCE_ERROR -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                }
            }
        })
    }

    val REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        tv_start_trial.setOnClickListener(View.OnClickListener {
            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().getInstance().execute(GetClientTokenForPayment())
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)

                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().getInstance().execute(SendPaymentNonceToServer(0.99f, result.paymentMethodNonce!!.nonce))
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode === Activity.RESULT_CANCELED) {

            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
            }
        }
    }
}
