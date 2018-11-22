package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.response.LoginResponse
import com.fuel4media.carrythistoo.model.response.PaymentTokenResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class GetClientTokenForPayment : Runnable {
    var TAG = GetClientTokenForPayment::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<PaymentTokenResponse>>? = null
        czResponse = ApiHelper.performGetPaymentToken() as CZResponse<ResponseBean<PaymentTokenResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.PAYMENT_TOKEN_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.PAYMENT_TOKEN_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}