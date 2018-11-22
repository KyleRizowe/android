package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class SendPaymentNonceToServer(var amount: Float, var nonce: String) : Runnable {
    var TAG = SendPaymentNonceToServer::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>
        czResponse = ApiHelper.sendPaymentNonceToServer(amount, nonce) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                UserManager.getInstance().setUserType(2)
                EventBus.getDefault().post(EventObject(EventConstant.PAYMENT_NONCE_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.PAYMENT_NONCE_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}