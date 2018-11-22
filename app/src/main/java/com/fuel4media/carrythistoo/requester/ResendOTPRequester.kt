package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.LoginRequest
import com.fuel4media.carrythistoo.model.response.LoginResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

/**
 * Created by shweta on 6/6/18.
 */
class ResendOTPRequester(val resendRequest: LoginRequest) : Runnable {
    var TAG = ResendOTPRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<LoginResponse>>? = null
        czResponse = ApiHelper.performResendOTP(resendRequest) as CZResponse<ResponseBean<LoginResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.RESEND_OTP_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.RESEND_OTP_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}