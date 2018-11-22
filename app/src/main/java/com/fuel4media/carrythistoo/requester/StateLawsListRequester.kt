package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.response.StateLawsListing
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class StateLawsListRequester(val stateID: String) : Runnable {
    var TAG = PermitListRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<StateLawsListing>>? = null
        czResponse = ApiHelper.performStateLawsList(stateID) as CZResponse<ResponseBean<StateLawsListing>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.STATE_LAWS_LIST_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.STATE_LAWS_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}