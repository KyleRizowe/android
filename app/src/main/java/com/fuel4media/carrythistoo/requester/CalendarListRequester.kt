package com.fuel4media.carrythistoo.requester

import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.request.CalendarRequest
import com.fuel4media.carrythistoo.model.response.CalendarResponse
import com.fuel4media.carrythistoo.network.ApiHelper
import com.fuel4media.carrythistoo.network.CZResponse
import com.fuel4media.carrythistoo.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class CalendarListRequester(val calendarRequest: CalendarRequest) : Runnable {
    var TAG = CalendarListRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<CalendarResponse>>? = null
        czResponse = ApiHelper.performCalendarList(calendarRequest) as CZResponse<ResponseBean<CalendarResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus()) {
                EventBus.getDefault().post(EventObject(EventConstant.CALENDAR_LIST_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.CALENDAR_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}