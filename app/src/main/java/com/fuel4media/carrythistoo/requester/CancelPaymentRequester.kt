package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.response.PaymentTokenResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class CancelPaymentRequester : Runnable {
    var TAG = GetClientTokenForPayment::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.cancelPaymentPlan() as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                UserManager.getInstance().setUserType(4);
                EventBus.getDefault().post(EventObject(EventConstant.CANCEL_PAYMENT_SUCCESS, czResponse!!.getResponse().getMessage()))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.CANCEL_PAYMENT_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}