package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.response.GunFreeZone
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class GetNearByGunZoneRequester(val lat: Double, val long: Double) : Runnable {
    var TAG = GetNearByGunZoneRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<ArrayList<GunFreeZone>>>? = null
        czResponse = ApiHelper.getGunFreeZone(lat, long) as CZResponse<ResponseBean<ArrayList<GunFreeZone>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.GUN_FREE_ZONE_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.GUN_FREE_ZONE_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            //EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}