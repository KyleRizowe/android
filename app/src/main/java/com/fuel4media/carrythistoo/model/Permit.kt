package com.fuel4media.carrythistoo.model

import android.os.Parcel
import android.os.Parcelable

class Permit() : Parcelable {
    var id: String? = null
    var state_name: String? = null
    var state_id: String? = null
    var permit_id: String? = null
    var permit_type: String? = null
    var permit_image: String? = null
    var permit_name: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        state_name = parcel.readString()
        state_id = parcel.readString()
        permit_id = parcel.readString()
        permit_type = parcel.readString()
        permit_image = parcel.readString()
        permit_name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(state_name)
        parcel.writeString(permit_id)
        parcel.writeString(permit_type)
        parcel.writeString(permit_image)
        parcel.writeString(permit_name)
        parcel.writeString(state_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Permit> {
        override fun createFromParcel(parcel: Parcel): Permit {
            return Permit(parcel)
        }

        override fun newArray(size: Int): Array<Permit?> {
            return arrayOfNulls(size)
        }
    }
}