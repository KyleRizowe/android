package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.StateLaws
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class ComapreStateLawsRequester(val state1: String, val state2: String) : Runnable {
    var TAG = ComapreStateLawsRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<ArrayList<StateLaws>>>? = null
        czResponse = ApiHelper.performCompareStateLawsList(state1, state2) as CZResponse<ResponseBean<ArrayList<StateLaws>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.COMAPRE_STATE_LAWS_LIST_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.COMPARE_STATE_LAWS_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}