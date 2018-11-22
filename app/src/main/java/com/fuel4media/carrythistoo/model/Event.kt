package com.fuel4media.carrythistoo.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by shweta on 30/5/18.
 */
class Event() : Parcelable {
    var id: String? = null
    var event_name: String? = null
    var descriptions: String? = null
    var date: String? = null
    var timestamp: Long = 0
    var addlocation: String? = null
    var user_id: String? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0
    var time: String? = null
    var notify: Int? = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        event_name = parcel.readString()
        descriptions = parcel.readString()
        date = parcel.readString()
        timestamp = parcel.readLong()
        addlocation = parcel.readString()
        user_id = parcel.readString()
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        long = parcel.readValue(Double::class.java.classLoader) as? Double
        time = parcel.readString()
        notify = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(event_name)
        parcel.writeString(descriptions)
        parcel.writeString(date)
        parcel.writeLong(timestamp)
        parcel.writeString(addlocation)
        parcel.writeString(user_id)
        parcel.writeValue(lat)
        parcel.writeValue(long)
        parcel.writeString(time)
        parcel.writeValue(notify)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

}