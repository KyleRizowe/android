package com.fuel4media.carrythistoo.model.request

class InviteOwners {
    var mobile_numbers = ArrayList<String>()

    constructor(mobileNumber: ArrayList<String>) {
        this.mobile_numbers = mobileNumber
    }
}