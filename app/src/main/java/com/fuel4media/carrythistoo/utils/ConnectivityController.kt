package com.fuel4media.carrythistoo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

/**
 * Created by craterzone on 29/9/16.
 */

object ConnectivityController {

    val TAG = ConnectivityController::class.java.name
    /**
     * Checking is connected to Internet
     * @param context
     * @return boolean
     */
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unable to check is internet Avaliable: \n" + e.message)
        }

        return false
    }
}
