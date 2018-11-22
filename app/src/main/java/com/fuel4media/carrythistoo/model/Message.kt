package com.fuel4media.carrythistoo.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by shweta on 30/5/18.
 */
class Message() : Parcelable{
    var messages : String? = null
    var message_type : String? = null
    var id : Int? = 0
    var user_id : Int? = 0
    var timestamp : Long? = 0
    var date : String? = null
    var status : String? = null

    constructor(parcel: Parcel) : this() {
        messages = parcel.readString()
        message_type = parcel.readString()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        user_id = parcel.readValue(Int::class.java.classLoader) as? Int
        timestamp = parcel.readValue(Long::class.java.classLoader) as? Long
        date = parcel.readString()
        status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messages)
        parcel.writeString(message_type)
        parcel.writeValue(id)
        parcel.writeValue(user_id)
        parcel.writeValue(timestamp)
        parcel.writeString(date)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }


}