package com.fuel4media.carrythistoo.activity;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fuel4media.carrythistoo.executer.BackgroundExecutor;
import com.fuel4media.carrythistoo.prefrences.AppPreference;
import com.fuel4media.carrythistoo.requester.UpdateLatLongRequester;
import com.google.android.gms.location.LocationResult;

import java.util.List;

public class LocationUpdateService extends IntentService {

    static final String ACTION_PROCESS_UPDATES =
            "PROCESS_UPDATES";
    private static final String TAG = LocationUpdateService.class.getSimpleName();


    public LocationUpdateService() {
        // Name the worker thread.
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    Log.d(TAG, "Location : lat = " + locations.get(0).getLatitude() + "long = " + locations.get(0).getLongitude());
                   /* LocationResultHelper locationResultHelper = new LocationResultHelper(this,
                            locations);
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults();
                    // Show notification with the location data.
                    locationResultHelper.showNotification();
                    Log.i(TAG, LocationResultHelper.getSavedLocationResult(this));*/


                    Intent reciver = new Intent("location");
                    // You can also include some extra data.
                    reciver.putExtra("location", locations.get(0));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(reciver);

                    updateLocationToPref(locations.get(0));
                    updateLocationToServer(locations.get(0));
                }
            }
        }
    }

    private void updateLocationToServer(Location location) {
        if (AppPreference.getInstance().isLogin()) {
            new BackgroundExecutor().getInstance().execute(new UpdateLatLongRequester(location.getLatitude(), location.getLongitude()));
        }
    }

    private void updateLocationToPref(Location location) {
        AppPreference.getInstance().setLatitude(location.getLatitude());
        AppPreference.getInstance().setLongitude(location.getLongitude());
    }
}
