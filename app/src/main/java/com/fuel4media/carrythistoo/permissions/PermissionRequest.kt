package com.fuel4media.carrythistoo.permissions

/**
 * Created by craterzone on 2/4/16.
 */
interface PermissionRequest {

    interface RequestCamera : BasePermissionsListener {
        fun onCameraPermissionGranted()

        fun onCameraPermissionDenied()
    }

    interface RequestContact : BasePermissionsListener {
        fun onContactPermissionGranted()

        fun onContactPermissionDenied()
    }

    interface RequestLocation : BasePermissionsListener {
        fun onLocationPermissionGranted()

        fun onLocationPermissionDenied()
    }

    interface RequestPhone : BasePermissionsListener {
        fun onPhonePermissionGranted()

        fun onPhonePermissionDenied()
    }

    interface RequestSms : BasePermissionsListener {
        fun onSmsPermissionGranted()

        fun onSmsPermissionDenied()
    }

    interface RequestCalender : BasePermissionsListener {
        fun onCalenderPermissionGranted()

        fun onCalenderPermissionDenied()
    }

    interface RequestSensor : BasePermissionsListener {
        fun onSensorPermissionGranted()

        fun onSensorPermissionDenied()
    }

    interface RequestMicrophone : BasePermissionsListener {
        fun onMicrophonePermissionGranted()

        fun onMicrophonePermissionDenied()
    }

    interface RequestStorage : BasePermissionsListener {
        fun onStoragePermissionGranted()

        fun onStoragePermissionDenied()
    }

    interface RequestCustomPermissionGroup : BasePermissionsListener {
        fun onAllCustomPermissionGroupGranted()

        fun onCustomPermissionGroupDenied()
    }

    interface RequestCameraStoragePermissionGroup : BasePermissionsListener {
        fun onAllCameraStoragePermissionGroupGranted()
        fun onCameraStoragePermissionGroupDenied()
    }

}
