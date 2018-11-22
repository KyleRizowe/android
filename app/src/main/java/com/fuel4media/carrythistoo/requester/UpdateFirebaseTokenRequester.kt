package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class UpdateFirebaseTokenRequester(val deviceID: String, val deviceToken: String) : Runnable {
    var TAG = UpdateFirebaseTokenRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>
        czResponse = ApiHelper.updateFirebaseToken(deviceID, deviceToken) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_FIREBASE_TOKEN_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_FIREBASE_TOKEN_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}