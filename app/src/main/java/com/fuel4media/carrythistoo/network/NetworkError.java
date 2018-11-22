package com.fuel4media.carrythistoo.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.HttpException;

/**
 * Created by kiwitech on 29/8/17.
 */

public class NetworkError {

    public static String getErrorMessage(Throwable throwable) {
        if (throwable == null) return "";
        String error = "Error";
        JSONObject jsonObject;
        try {
            if (throwable instanceof HttpException) {
                jsonObject = new JSONObject(((HttpException) throwable).response().errorBody().string());
                if (jsonObject.has("errors")) {
                    error = showErrorKeyMessage(jsonObject);
                } else {
                    error = jsonObject.getString("detail");
                }
            } else if (throwable instanceof IOException) {
                error = throwable.getMessage();
            } else {
                error = throwable.getMessage();
            }
        } catch (Exception e) {
            error = throwable.getMessage();
        } finally {
            return error;
        }
    }

    public static String showErrorKeyMessage(JSONObject jsonObject) {
        String error = "Error";
        try {
            JSONObject jsonErrorObject = jsonObject.optJSONObject("errors");

            if(jsonErrorObject != null && jsonErrorObject.get("error") instanceof JSONArray){
                JSONArray jsonArray = jsonErrorObject.getJSONArray("error");
                error = jsonArray.getString(0);
            }else{
                error = jsonErrorObject.get("error").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error;
    }

}
