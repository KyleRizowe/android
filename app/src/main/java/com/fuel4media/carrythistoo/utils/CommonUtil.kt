package com.fuel4media.carrythistoo.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

/**
 * Created by Bheesham on 16/02/17.
 */

object CommonUtil {

    private var screenWidth = 0
    private var screenHeight = 0

    val isAndroid5: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }

        return screenHeight
    }

    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }

        return screenWidth
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /*    public static String getDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }*/

}
