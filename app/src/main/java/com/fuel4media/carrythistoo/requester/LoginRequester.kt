package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.request.LoginRequest
import com.fuel4media.carrythistoo.model.response.LoginResponse
import com.fuel4media.carrythistoo.model.response.UserResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import com.fuel4media.carrythistoo.prefrences.AppPreference
import org.greenrobot.eventbus.EventBus

/**
 * Created by shweta on 1/6/18.
 */
class LoginRequester(val loginRequest: LoginRequest) : Runnable {
    var TAG = LoginRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<LoginResponse>>? = null
        czResponse = ApiHelper.performLogin(loginRequest) as CZResponse<ResponseBean<LoginResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            val userResponse = czResponse!!.getResponse().data
            if (czResponse!!.getResponse().getStatus()) {
              /*  UserManager.getInstance().setUser(userResponse.user)
                UserManager.getInstance().saveSettings(userResponse.settings)
                UserManager.getInstance().saveState(userResponse.states)
                UserManager.getInstance().savePermits(userResponse.permit_types)
                UserManager.getInstance().saveFilters(userResponse.filter_type)
                AppPreference.getInstance().setLogin(true)
                AppPreference.getInstance().setSessionToken(userResponse.token)*/
                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_SUCCESS, userResponse))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}