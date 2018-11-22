package com.fuel4media.carrythistoo.network;

import android.content.res.Resources;

import com.fuel4media.carrythistoo.CarryThisTooApplication;
import com.fuel4media.carrythistoo.R;

/**
 * Created by kiwitech on 09/8/17.
 */

public interface NetworkConstants {
    Resources resources = CarryThisTooApplication.Companion.applicationContext().getResources();

    String APP_SERVER_URL = resources.getString(R.string.app_server_url);

    static final int RETROFIT_API_SERVICE_SOCKET_TIMEOUT = 120;
    public static final int ION_MEDIA_UPLOAD_TIMEOUT = 3 * 60 * 1000;

    /* HEADERS PARAMS*/
    public static final String HEADER_PARAM_KEY_TOKEN = "Authorization";
    public static final String HEADER_PARAM_KEY_ACCEPT = "Accept";
    public static final String HEADER_PARAM_VALUE_ACCEPT = "application/json";

}
