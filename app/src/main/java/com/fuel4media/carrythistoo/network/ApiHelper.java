package com.fuel4media.carrythistoo.network;


import com.fuel4media.carrythistoo.model.Event;
import com.fuel4media.carrythistoo.model.Message;
import com.fuel4media.carrythistoo.model.Permit;
import com.fuel4media.carrythistoo.model.Settings;
import com.fuel4media.carrythistoo.model.request.CalendarRequest;
import com.fuel4media.carrythistoo.model.request.Establisment;
import com.fuel4media.carrythistoo.model.request.GunZoneRequest;
import com.fuel4media.carrythistoo.model.request.InviteOwners;
import com.fuel4media.carrythistoo.model.request.LoginRequest;
import com.fuel4media.carrythistoo.model.request.RepoMapRequest;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by kiwitech on 09/8/17.
 */

public class ApiHelper {

    private static final String TAG = ApiHelper.class.getSimpleName();

    /**
     * perform user loginRequest.
     *
     * @param loginRequest
     * @return
     */

    public static CZResponse performLogin(LoginRequest loginRequest) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().performLogin(loginRequest));
    }

    public static CZResponse performRegister(LoginRequest registerRequest) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().performRegister(registerRequest));
    }


    public static CZResponse performVerifyOtp(LoginRequest loginRequest) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().performVerifyOTP(loginRequest));
    }

    public static CZResponse updateSettings(Settings settings) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().updateSettings(settings));
    }

    public static CZResponse updateLatLong(Double lat, Double lon) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().updateLatLong(lat, lon));
    }

    public static CZResponse performResendOTP(LoginRequest resendRequset) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().resendOTP(resendRequset));
    }

    public static CZResponse performAddEvent(Event addEvent) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().addEvent(addEvent));
    }

    public static CZResponse performInviteOwners(InviteOwners inviteOwners) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().inviteOwners(inviteOwners));
    }

    public static CZResponse performAddEstablisment(Establisment establisment) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().addEstablisment(establisment));
    }

    public static CZResponse performEditEvent(Event editEvent) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().editEvent(editEvent.getId(), editEvent));
    }

    public static CZResponse performDeleteEvent(String eventID) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().deleteEvent(eventID));
    }

    public static CZResponse performPermitList() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().permitList());
    }

    public static CZResponse performStateLawsList(String stateID) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().stateLawList(stateID));
    }

    public static CZResponse performCompareStateLawsList(String state1, String state2) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().stateCompareLawsList(state1, state2));
    }


    public static CZResponse performAddPermit(Permit permit) {
        MultipartBody.Part body = null;
        if (permit.getPermit_image() != null) {
            File file = new File(permit.getPermit_image());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("permit_image", file.getName(), requestFile);
        }

// add another part within the multipart request
        RequestBody stateName =
                RequestBody.create(MediaType.parse("multipart/form-data"), permit.getState_name());

        RequestBody permitID =
                RequestBody.create(MediaType.parse("multipart/form-data"), permit.getPermit_id());

        RequestBody permitType =
                RequestBody.create(MediaType.parse("multipart/form-data"), permit.getPermit_type());

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().addPermit(stateName, permitID, permitType, body));
    }


    public static CZResponse performEditPermit(Permit editPermit) {
        MultipartBody.Part body = null;
        if (editPermit.getPermit_image() != null) {
            File file = new File(editPermit.getPermit_image());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("permit_image", file.getName(), requestFile);
        }

// add another part within the multipart request
        RequestBody stateName =
                RequestBody.create(MediaType.parse("multipart/form-data"), editPermit.getState_name());

        RequestBody permitID =
                RequestBody.create(MediaType.parse("multipart/form-data"), editPermit.getPermit_id());

        RequestBody permitType =
                RequestBody.create(MediaType.parse("multipart/form-data"), editPermit.getPermit_type());
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().editPermit(editPermit.getId(), stateName, permitID, permitType, body));
    }

    public static CZResponse performDeletePermit(String permitID) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().deletePermit(permitID));
    }

    public static CZResponse performCalendarList(CalendarRequest calendarRequest) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().calendarList(calendarRequest));
    }

    public static CZResponse performEventList(CalendarRequest calendarRequest) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().eventList(calendarRequest));
    }

    public static CZResponse performGunZone(GunZoneRequest gunZoneRequest) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().filterGunZone(gunZoneRequest));
    }

    public static CZResponse getGunFreeZone(Double lat, Double log) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getGunFreeZone(lat, log));
    }

    public static CZResponse performSendMessages(Message message) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().sendMessage(message));
    }

    public static CZResponse performRepoMap(RepoMapRequest repoMapRequest) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getRepoMap(repoMapRequest));
    }

    public static CZResponse performAddSuggestion(String suggestion) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().addSuggestion(suggestion));
    }

    public static CZResponse performMessageList() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getMessages());
    }

    public static CZResponse performGetPaymentToken() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getPaymentToken());
    }

    public static CZResponse cancelPaymentPlan() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().cancelPaymentPlan());
    }

    public static CZResponse startPremiumPlan() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().startPremiumPlan());
    }

    public static CZResponse sendPaymentNonceToServer(float amount, String nonce) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().sendPaymentNonce(amount, nonce));
    }

    public static CZResponse updateFirebaseToken(String deviceID, String deviceToken) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().updateFirebaseToken(deviceID, deviceToken));
    }

    public static CZResponse performLogout() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().logout());
    }
}
