package com.fuel4media.carrythistoo.model.response

import com.fuel4media.carrythistoo.model.Dates
import com.fuel4media.carrythistoo.model.Event

class CalendarResponse {
    var dates_list: ArrayList<Dates>? = null
    var upcomings: ArrayList<Event>? = null
    var history: ArrayList<Event>? = null

}