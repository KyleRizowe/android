package com.fuel4media.carrythistoo.model

import android.os.Parcel
import android.os.Parcelable

class State : Parcelable {
    var state_id: String? = null
    var state_name: String? = null
    var status: Boolean? = false

    constructor(parcel: Parcel) : this() {
        state_id = parcel.readString()
        state_name = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }


    constructor()

    constructor(id: String) {
        this.state_id = id
    }

    constructor(id: String, name: String) {
        this.state_id = id
        this.state_name = name
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (state_id != other.state_id) return false

        return true
    }

    override fun hashCode(): Int {
        return state_id?.hashCode() ?: 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(state_id)
        parcel.writeString(state_name)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<State> {
        override fun createFromParcel(parcel: Parcel): State {
            return State(parcel)
        }

        override fun newArray(size: Int): Array<State?> {
            return arrayOfNulls(size)
        }
    }

}