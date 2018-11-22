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
 * Created by shweta on 2/6/18.
 */
class RegisterRequester(val register: LoginRequest) : Runnable {
    var TAG = RegisterRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<LoginResponse>>? = null
        czResponse = ApiHelper.performRegister(register) as CZResponse<ResponseBean<LoginResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().status) {
                EventBus.getDefault().post(EventObject(EventConstant.REGISTER_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.REGISTER_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}