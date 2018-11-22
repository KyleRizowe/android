package com.cz.imagelib;

class Log {

    private static final String TAG = "android-crop";

    public static void e(String msg) {
        android.util.Log.e(TAG, msg);
    }
    public static void d(String msg) {
        android.util.Log.d(TAG, msg);
    }

    public static void e(String msg, Throwable e) {
        android.util.Log.e(TAG, msg, e);
    }

}
