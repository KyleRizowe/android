package com.fuel4media.carrythistoo.model.response

import android.os.Parcel
import android.os.Parcelable
import android.support.design.internal.ParcelableSparseArray

class GunFreeZone() : Parcelable {
    var gun_key_id: String? = null
    var gun_subkey_id: String? = null
    var state: String? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0
    var zone_name: String? = null
    var distance: Double? = null

    constructor(parcel: Parcel) : this() {
        gun_key_id = parcel.readString()
        gun_subkey_id = parcel.readString()
        state = parcel.readString()
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        long = parcel.readValue(Double::class.java.classLoader) as? Double
        distance = parcel.readValue(Double::class.java.classLoader) as? Double
        zone_name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gun_key_id)
        parcel.writeString(gun_subkey_id)
        parcel.writeString(state)
        parcel.writeValue(lat)
        parcel.writeValue(long)
        parcel.writeValue(distance)
        parcel.writeString(zone_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GunFreeZone> {
        override fun createFromParcel(parcel: Parcel): GunFreeZone {
            return GunFreeZone(parcel)
        }

        override fun newArray(size: Int): Array<GunFreeZone?> {
            return arrayOfNulls(size)
        }
    }
}