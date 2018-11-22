package com.fuel4media.carrythistoo.prefrences;


import android.text.TextUtils;

import com.fuel4media.carrythistoo.CarryThisTooApplication;
import com.fuel4media.carrythistoo.manager.LogManager;
import com.fuel4media.carrythistoo.model.FilterType;
import com.fuel4media.carrythistoo.model.PermitType;
import com.fuel4media.carrythistoo.model.Settings;
import com.fuel4media.carrythistoo.model.State;
import com.fuel4media.carrythistoo.model.User;
import com.fuel4media.carrythistoo.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


/**
 * Created by craterzone on 21/9/16.
 */

public class AppPreference extends BasePreferences {

    public final static String TAG = AppPreference.class.getSimpleName();

    private static AppPreference mInstance;

    private static final String SHARED_PREF_NAME = "carry_this_too_pref";
    private static final int PRIVATE_MODE = 0;


    private interface Keys {
        String DEVICE_TOKEN = "device_token";
        String SESSION_TOKEN = "session_token";
        String FIREBASE_TOKEN = "firebase_token";
        String DEVICE_ID = "device_id";
        String DEVICE_TYPE = "device_type";
        String USER_ID = "user_id";
        String EMAIL = "email";
        String USER_NAME = "user_name";
        String PASSWORD = "password";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String MOBILE_NO = "mobile_number";
        String PIN_NUMBER = "pin_number";
        String PROFILE_URL = "profile_url";
        String GENDER = "gender";
        String IS_LOGIN = "is_login";
        String IS_REMEMBER_ME = "is_remember_me";
        String IS_FIRST_TIME_USER = "is_first_time";
        String USER = "user";
        String STATES = "states";
        String PERMITS = "permits";
        String FILTERS = "filters";
        String IS_FINGER_PRINT_ENABLE = "finger_print_setting";
        String MESSAGE_COUNTER = "message_counter";
        String ACTION_COUNTER = "action_counter";
        String FEEDBACK_STATUS = "feedback";
        String SETTINGS = "settings";
        String LATITUDE = "lat";
        String LONGITUDE = "long";
    }

    private AppPreference() {
        super(CarryThisTooApplication.Companion.applicationContext().
                getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE));
    }

    public static AppPreference getInstance() {
        if (mInstance == null) {
            synchronized (AppPreference.class) {
                if (mInstance == null) {
                    mInstance = new AppPreference();
                }
            }
        }
        return mInstance;
    }

    public void setDeviceId(String deviceId) {
        setString(Keys.DEVICE_ID, null);
    }

    public String getDeviceID() {
        return getString(Keys.DEVICE_ID, null);
    }


    public String getDeviceToken() {
        return getString(Keys.DEVICE_TOKEN, null);
    }

    public void setFirebaseToken(String token) {
        LogManager.d(TAG, "save firebase token value: " + token);
        setString(Keys.FIREBASE_TOKEN, token);
    }

    public String getFirebaseToken() {
        return getString(Keys.FIREBASE_TOKEN, null);
    }

    public void setDeviceToken(String token) {
        LogManager.d(TAG, "save device token value: " + token);
        setString(Keys.DEVICE_TOKEN, token);
    }

    public void setLatitude(Double latitude) {
        LogManager.d(TAG, "save latitude value: " + latitude);
        setDouble(Keys.LATITUDE, latitude);
    }

    public void setLongitude(Double longitude) {
        LogManager.d(TAG, "save longitude value: " + longitude);
        setDouble(Keys.LONGITUDE, longitude);
    }

    public Double getLatitude() {
        return getDouble(Keys.LATITUDE, 0.0);
    }

    public Double getLongitude() {
        return getDouble(Keys.LONGITUDE, 0.0);
    }


    public void setSessionToken(String token) {
        LogManager.d(TAG, "save session token value: " + token);
        setString(Keys.SESSION_TOKEN, token);
    }

    public void setFingerPrintStatus(boolean status) {
        LogManager.d(TAG, "save fingerprint status value: " + status);
        setBoolean(Keys.IS_FINGER_PRINT_ENABLE, status);
    }

    public String getSessionToken() {
        return getString(Keys.SESSION_TOKEN, null);
    }

    public boolean isFingerPrintEnable() {
        return getBoolean(Keys.IS_FINGER_PRINT_ENABLE, false);
    }


    public long getUserId() {
        return getLong(Keys.USER_ID, 0);
    }

    public void setUserId(long userId) {
        LogManager.d(TAG, "save user id value: " + userId);
        setLong(Keys.USER_ID, userId);
    }

    public void setEmail(String email) {
        LogManager.d(TAG, "save email value: " + email);
        setString(Keys.EMAIL, email);
    }

    public String getEmail() {
        return getString(Keys.EMAIL, "");
    }

    public void setFirstName(String name) {
        LogManager.d(TAG, "save name value: " + name);
        setString(Keys.FIRST_NAME, name);
    }

    public String getFirstName() {
        return getString(Keys.FIRST_NAME, "USER");
    }

    public void setLastName(String lastName) {
        LogManager.d(TAG, "save last name value: " + lastName);
        setString(Keys.LAST_NAME, lastName);
    }

    public String getLastName() {
        return getString(Keys.LAST_NAME, "");
    }


    public void setPinNumber(String pinNumber) {
        LogManager.d(TAG, "save pin number value: " + pinNumber);
        setString(Keys.PIN_NUMBER, pinNumber);
    }

    public String getUserName() {
        return getString(Keys.USER_NAME, "");
    }

    public void setUserName(String userName) {
        LogManager.d(TAG, "save username value: " + userName);
        setString(Keys.USER_NAME, userName);
    }

    public String getPinNumber() {
        return getString(Keys.PIN_NUMBER, "");
    }

    public String getMobileNumber() {
        return getString(Keys.MOBILE_NO, "");
    }

    public void setMobileNumber(String mobileNumber) {
        LogManager.d(TAG, "save mobile number  value: " + mobileNumber);
        setString(Keys.MOBILE_NO, mobileNumber);
    }

    public String getPassword() {
        return getString(Keys.PASSWORD, "");
    }

    public void setPassword(String password) {
        LogManager.d(TAG, "save password  value: " + password);
        setString(Keys.PASSWORD, password);
    }

    public void setProfileUrl(String profileUrl) {
        LogManager.d(TAG, "save profile pic url value: " + profileUrl);
        setString(Keys.PROFILE_URL, profileUrl);
    }

    public void setLogin(boolean login) {
        LogManager.d(TAG, "save loginRequest status value: " + login);
        setBoolean(Keys.IS_LOGIN, login);
    }


    public boolean isLogin() {
        return getBoolean(Keys.IS_LOGIN, false);
    }

    public void setFeedbackStatus(boolean status) {
        LogManager.d(TAG, "save feedback status value: " + status);
        setBoolean(Keys.FEEDBACK_STATUS, status);
    }

    public boolean getFeedbackStatus() {
        return getBoolean(Keys.FEEDBACK_STATUS, false);
    }

    public void setFirstTimeUser(boolean isFirst) {
        LogManager.d(TAG, "save first time user status: " + isFirst);
        setBoolean(Keys.IS_FIRST_TIME_USER, isFirst);
    }

    public boolean isFirstTimeUser() {
        return getBoolean(Keys.IS_FIRST_TIME_USER, true);
    }

    public void setRememberMe(boolean rememberMe) {
        LogManager.d(TAG, "save remember me status: " + rememberMe);
        setBoolean(Keys.IS_REMEMBER_ME, rememberMe);
    }

    public boolean getRememberMeStatus() {
        return getBoolean(Keys.IS_REMEMBER_ME, false);
    }


    public String getProfileUrl() {
        return getString(Keys.PROFILE_URL, "");
    }

    public void setUser(User user) {
        setString(Keys.USER, GsonUtil.toJson(user));
    }

    public void saveStates(ArrayList<State> states) {
        setString(Keys.STATES, GsonUtil.toJson(states));
    }

    public void savePermits(ArrayList<PermitType> permits) {
        setString(Keys.PERMITS, GsonUtil.toJson(permits));
    }

    public void saveFilters(ArrayList<FilterType> filters) {
        setString(Keys.FILTERS, GsonUtil.toJson(filters));
    }

    public void saveSettings(Settings settings) {
        setString(Keys.SETTINGS, GsonUtil.toJson(settings));
    }

    public void setMessageCounter(int message_count) {
        setInt(Keys.MESSAGE_COUNTER, message_count);
    }

    public void setActionCounter(int action_count) {
        setInt(Keys.ACTION_COUNTER, action_count);
    }

    public int getMessageCounter() {
        return getInt(Keys.MESSAGE_COUNTER, 0);
    }

    public int getActionCounter() {
        return getInt(Keys.ACTION_COUNTER, 0);
    }

    public User getUser() {

        String userString = getString(Keys.USER, "");

        if (!TextUtils.isEmpty(userString)) {
            return (User) GsonUtil.toModel(userString, User.class);
        }

        return null;
    }

    public ArrayList<State> getStates() {

        String userString = getString(Keys.STATES, "");

        if (!TextUtils.isEmpty(userString)) {
            return (ArrayList<State>) GsonUtil.toModel(userString, new TypeToken<ArrayList<State>>() {
            }.getType());
        }

        return null;
    }

    public ArrayList<PermitType> getPermits() {

        String userString = getString(Keys.PERMITS, "");

        if (!TextUtils.isEmpty(userString)) {
            return (ArrayList<PermitType>) GsonUtil.toModel(userString, new TypeToken<ArrayList<PermitType>>() {
            }.getType());
        }

        return null;
    }

    public ArrayList<FilterType> getFilters() {

        String userString = getString(Keys.FILTERS, "");

        if (!TextUtils.isEmpty(userString)) {
            return (ArrayList<FilterType>) GsonUtil.toModel(userString, new TypeToken<ArrayList<FilterType>>() {
            }.getType());
        }

        return null;
    }


    public Settings getSettings() {

        String userString = getString(Keys.SETTINGS, "");

        if (!TextUtils.isEmpty(userString)) {
            return (Settings) GsonUtil.toModel(userString, Settings.class);
        }

        return null;
    }

}
