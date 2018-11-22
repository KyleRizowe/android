package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class UpdateLatLongRequester(val lat: Double, val long: Double) : Runnable {
    var TAG = UpdateLatLongRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>
        czResponse = ApiHelper.updateLatLong(lat, long) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_LOCATION_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_LOCATION_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
           // EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }

}