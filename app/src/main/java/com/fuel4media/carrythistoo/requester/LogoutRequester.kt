package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

/**
 * Created by shweta on 6/6/18.
 */
class LogoutRequester() : Runnable {
    var TAG = LogoutRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>
        czResponse = ApiHelper.performLogout() as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                UserManager.getInstance().logout()
                EventBus.getDefault().post(EventObject(EventConstant.LOGOUT_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.LOGOUT_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}