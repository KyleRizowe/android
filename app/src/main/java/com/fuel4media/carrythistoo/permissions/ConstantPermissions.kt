package com.fuel4media.carrythistoo.permissions

import android.Manifest

/**
 * Created by craterzone on 24/2/16.
 */
interface ConstantPermissions {
    companion object {

        /*permission-group-request-code*/
        const val REQUEST_CAMERA_CODE = 100
        const val REQUEST_CONTACTS_CODE = 101
        const val REQUEST_PHONE_CODE = 102
        const val REQUEST_LOCATION_CODE = 103
        const val REQUEST_SMS_CODE = 104
        const val REQUEST_SENSORS_CODE = 105
        const val REQUEST_MICROPHONE_CODE = 106
        const val REQUEST_STORAGE_CODE = 107
        const val REQUEST_CALENDER_CODE = 108
        const val REQUEST_CUSTOM_PERMISSION_GROUP_CODE = 109
        const val REQUEST_CAMERA_STORAGE_GROUP_CODE = 110


        /*permission-group-request-string*/
        val CONTACTS_PERMISSION_STRING = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS)
        val STORAGE_PERMISSION_STRING = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val CALENDER_PERMISSION_STRING = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        val SMS_PERMISSION_STRING = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.RECEIVE_MMS, Manifest.permission.RECEIVE_WAP_PUSH)
        val MICROPHONE_PERMISSION_STRING = arrayOf(Manifest.permission.RECORD_AUDIO)
        val SENSOR_PERMISSION_STRING = arrayOf(Manifest.permission.BODY_SENSORS, Manifest.permission.USE_FINGERPRINT)
        val PHONE_PERMISSION_STRING1 = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS)
        val PHONE_PERMISSION_STRING = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)
        val LOCATION_PERMISSION_STRING = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        val CAMERA_PERMISSION_STRING = arrayOf(Manifest.permission.CAMERA)
        val CAMERA_PERMISSION_EXTERNAL_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }
}
