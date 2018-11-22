package com.fuel4media.carrythistoo.permissions

/**
 * Created by craterzone on 4/4/16.
 */
object PermissionRequestHandler {

    /*
    * Camera request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_CAMERA_CODE)
    fun requestCamera(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.CAMERA_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestCamera
            callback.onCameraPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_CAMERA_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_CAMERA_CODE)
    private fun requestCameraDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestCamera
        callback.onCameraPermissionDenied()
    }


    /*
    * Contact request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_CONTACTS_CODE)
    fun requestContact(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.CONTACTS_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestContact
            callback.onContactPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_CONTACTS_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_CONTACTS_CODE)
    private fun requestContactDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestContact
        callback.onContactPermissionDenied()
    }

    /*
 * Storage request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_STORAGE_CODE)
    fun requestStorage(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.STORAGE_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestStorage
            callback.onStoragePermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_STORAGE_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_STORAGE_CODE)
    private fun requestStorageDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestStorage
        callback.onStoragePermissionDenied()
    }

    /*
* Phone request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_PHONE_CODE)
    fun requestPhone(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.PHONE_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestPhone
            callback.onPhonePermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_PHONE_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_PHONE_CODE)
    private fun requestPhoneDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestPhone
        callback.onPhonePermissionDenied()
    }

    /*
* Sensor request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_SENSORS_CODE)
    fun requestSensor(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.SENSOR_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestSensor
            callback.onSensorPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_SENSORS_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_SENSORS_CODE)
    private fun requestSensorDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestSensor
        callback.onSensorPermissionDenied()
    }


    /*
* Location request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_LOCATION_CODE)
    fun requestLocation(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.LOCATION_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestLocation
            callback.onLocationPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_LOCATION_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_LOCATION_CODE)
    private fun requestLocationDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestLocation
        callback.onLocationPermissionDenied()
    }


    /*
* SMS request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_SMS_CODE)
    fun requestSMS(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.SMS_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestSms
            callback.onSmsPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_SMS_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_SMS_CODE)
    private fun requestSMSDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestSms
        callback.onSmsPermissionDenied()
    }


    /*
* Calender request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_CALENDER_CODE)
    fun requestCalender(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.CALENDER_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestCalender
            callback.onCalenderPermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_CALENDER_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_CALENDER_CODE)
    private fun requestCalenderDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestCalender
        callback.onCalenderPermissionDenied()
    }


    /*
* Microphone request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_MICROPHONE_CODE)
    fun requestMicrophone(`object`: Any, rationalString: String) {
        val perms = ConstantPermissions.MICROPHONE_PERMISSION_STRING
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestMicrophone
            callback.onMicrophonePermissionGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_MICROPHONE_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_MICROPHONE_CODE)
    private fun requestMicrophoneDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestMicrophone
        callback.onMicrophonePermissionDenied()
    }

    /*
* Custom permission group request*/
    @AfterPermissionGranted(ConstantPermissions.REQUEST_CUSTOM_PERMISSION_GROUP_CODE)
    fun requestCustomPermissionGroup(`object`: Any, rationalString: String, vararg perms: String) {
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestCustomPermissionGroup
            callback.onAllCustomPermissionGroupGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_CUSTOM_PERMISSION_GROUP_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_CUSTOM_PERMISSION_GROUP_CODE)
    private fun requestCustomPermissionGroupDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestCustomPermissionGroup
        callback.onCustomPermissionGroupDenied()
    }

    /*
    * Custom permission group request
    *
    * */
    @AfterPermissionGranted(ConstantPermissions.REQUEST_CAMERA_STORAGE_GROUP_CODE)
    fun requestCameraStoragePermissionGroup(`object`: Any, rationalString: String, vararg perms: String) {
        if (PermissionsUtil.hasPermission(PermissionsUtil.getActivity(`object`)!!, *perms)) {
            val callback = `object` as PermissionRequest.RequestCameraStoragePermissionGroup
            callback.onAllCameraStoragePermissionGroupGranted()
        } else {
            PermissionsUtil.requestPermissions(`object`, rationalString,
                    ConstantPermissions.REQUEST_CAMERA_STORAGE_GROUP_CODE, *perms)
        }
    }

    @AfterPermissionDenied(ConstantPermissions.REQUEST_CAMERA_STORAGE_GROUP_CODE)
    private fun requestCameraStoragePermissionGroupDenied(`object`: Any) {
        val callback = `object` as PermissionRequest.RequestCameraStoragePermissionGroup
        callback.onCameraStoragePermissionGroupDenied()
    }

}
