package com.fuel4media.carrythistoo.model

class PermitType {
    var permit_id: String? = null
    var permit_name: String? = null

    constructor(id: String) {
        this.permit_id = id
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PermitType

        if (permit_id != other.permit_id) return false

        return true
    }

    override fun hashCode(): Int {
        return permit_id?.hashCode() ?: 0
    }
}