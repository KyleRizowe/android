package com.fuel4media.carrythistoo.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by shweta on 31/5/18.
 */
class Contact : Parcelable {
    var id: String? = null
    var name: String? = null
    var mobile_number: String? = null
    var status: Boolean? = false

    constructor()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        mobile_number = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(mobile_number)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }

}