package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.Establisment
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class AddEstablishmentRequester(val establisment: Establisment) : Runnable {
    var TAG = AddEstablishmentRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.performAddEstablisment(establisment) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.ADD_ESTABLISMENT_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.ADD_ESTABLISMENT_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}