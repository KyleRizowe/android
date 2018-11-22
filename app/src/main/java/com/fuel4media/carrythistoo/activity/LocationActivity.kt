package com.fuel4media.carrythistoo.activity

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.fragments.MessageFragment
import com.fuel4media.carrythistoo.model.Message
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.requester.UpdateLatLongRequester
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


abstract class LocationActivity : BaseActivity() {

    protected lateinit var lastLocation: Location

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // 1
    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest

    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationReceiver,
                IntentFilter("location"));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation

                updateLocationToServer()
                updateLocationToPref()
                updateLocationCallback()
            }
        }

        createLocationRequest()
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, LocationUpdateService::class.java)
        intent.action = LocationUpdateService.ACTION_PROCESS_UPDATES
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun updateLocationToServer() {
        Log.d("Location ", "lat:" + lastLocation.latitude + " Long:" + lastLocation.longitude)
       // BackgroundExecutor().getInstance().execute(UpdateLatLongRequester(lastLocation.latitude, lastLocation.longitude))
    }

    private fun updateLocationToPref() {
        AppPreference.getInstance().latitude = lastLocation.latitude
        AppPreference.getInstance().longitude = lastLocation.longitude
    }

    abstract fun updateLocationCallback()

    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, getPendingIntent())
    }


    fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@LocationActivity,
                            REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private val mLocationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val location = intent.getParcelableExtra<Location>("location")

            lastLocation = location

            updateLocationCallback()

        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        //fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationReceiver);
    }
}
