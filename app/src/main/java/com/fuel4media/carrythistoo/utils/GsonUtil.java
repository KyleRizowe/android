package com.fuel4media.carrythistoo.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * This Class Handle JSON to Object and object to JSON conversion
 * @author cratrezone
 *
 */
public class GsonUtil {

    private static final String TAG = GsonUtil.class.getName();

    /**
     * This Method Convert Object in JSON String
     * @param object
     * @return Json String or null if some error
     */
    public static String toJson(Object object) {
        try {
            Gson gson = new Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, "Error in Converting Model to Json", e);
        }
        return null;
    }

    /**
     * This method convert json to model
     * @param json
     * @return Model or null if some error
     */
    public static <T> Object toModel(String json, Type listType) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error in Converting JSON to Model", e);
        }
        return null;
    }



}
