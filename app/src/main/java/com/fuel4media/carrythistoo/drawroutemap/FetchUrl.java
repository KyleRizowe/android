package com.fuel4media.carrythistoo.drawroutemap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
public class FetchUrl {
    public static String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=true";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&Key=AIzaSyCvvi1nQIAvhoYgaCnuMsjWJVbnC64HP9c";
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}
