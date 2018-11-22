package com.fuel4media.carrythistoo.model.request

class CalendarRequest {
    var from: String? = null
    var to: String? = null
    var date: String? = null

    constructor()

    constructor(from: String, to: String) {
        this.from = from;
        this.to = to;
    }

    constructor(date: String) {
        this.date = date;
    }
}