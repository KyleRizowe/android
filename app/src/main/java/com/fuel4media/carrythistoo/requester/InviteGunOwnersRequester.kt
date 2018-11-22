package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.InviteOwners
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class InviteGunOwnersRequester(val inviteOwners: InviteOwners) : Runnable {
    var TAG = InviteGunOwnersRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.performInviteOwners(inviteOwners) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.INVITE_OWNERS_SUCCESS, czResponse.response.message))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.INVITE_OWNERS_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }

}