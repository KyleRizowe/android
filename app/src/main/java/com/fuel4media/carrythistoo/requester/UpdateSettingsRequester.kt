package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Settings
import com.fuel4media.carrythistoo.network.ApiHelper.updateSettings
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

/**
 * Created by shweta on 6/6/18.
 */
class UpdateSettingsRequester(private val settings: Settings) : Runnable {
    var TAG = UpdateSettingsRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>
        czResponse = updateSettings(settings) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                UserManager.getInstance().updateSettings(settings)
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_SETTINGS_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.UPDATE_SETTINGS_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}