package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.RepoMapRequest
import com.fuel4media.carrythistoo.model.response.RepoMapResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class GetRepoMapRequester(val repoMapRequest: RepoMapRequest) : Runnable {
    var TAG = GetRepoMapRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<RepoMapResponse>>? = null
        czResponse = ApiHelper.performRepoMap(repoMapRequest) as CZResponse<ResponseBean<RepoMapResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.GET_REPO_MAP_SUCCESS, czResponse.response.data.repomap))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.GET_REPO_MAP_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}