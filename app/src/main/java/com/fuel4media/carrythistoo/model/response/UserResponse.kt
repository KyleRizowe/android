package com.fuel4media.carrythistoo.model.response

import com.fuel4media.carrythistoo.model.*

/**
 * Created by shweta on 6/6/18.
 */
class UserResponse {
    val token: String? = null
    val user: User? = null
    val settings: Settings? = null
    val states: ArrayList<State>? = null
    val permit_types: ArrayList<PermitType>? = null
    val filter_type: ArrayList<FilterType>? = null
}