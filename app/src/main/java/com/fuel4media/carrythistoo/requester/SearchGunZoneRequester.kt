package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.GunZone
import com.fuel4media.carrythistoo.model.request.GunZoneRequest
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class SearchGunZoneRequester(val gunZoneRequest: GunZoneRequest) : Runnable {
    var TAG = SearchGunZoneRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<ArrayList<GunZone>>>? = null
        czResponse = ApiHelper.performGunZone(gunZoneRequest) as CZResponse<ResponseBean<ArrayList<GunZone>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.FILTER_GUN_ZONE_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.FILTER_GUN_ZONE_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}