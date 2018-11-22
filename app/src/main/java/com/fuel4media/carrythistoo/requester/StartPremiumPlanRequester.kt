package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class StartPremiumPlanRequester : Runnable {
    var TAG = GetClientTokenForPayment::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.startPremiumPlan() as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                UserManager.getInstance().setUserType(3);
                EventBus.getDefault().post(EventObject(EventConstant.START_PREMIUM_SUCCESS, czResponse!!.getResponse().getMessage()))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.START_PREMIUM_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}