package com.fuel4media.carrythistoo.model

class FilterType {
    var filter_id: String? = null
    var filter_name: String? = null

    constructor(id: String) {
        this.filter_id = id
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilterType

        if (filter_id != other.filter_id) return false

        return true
    }

    override fun hashCode(): Int {
        return filter_id?.hashCode() ?: 0
    }
}