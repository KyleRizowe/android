package com.fuel4media.carrythistoo.network;

import com.fuel4media.carrythistoo.model.Event;
import com.fuel4media.carrythistoo.model.Message;
import com.fuel4media.carrythistoo.model.Permit;
import com.fuel4media.carrythistoo.model.Settings;
import com.fuel4media.carrythistoo.model.StateLaws;
import com.fuel4media.carrythistoo.model.request.CalendarRequest;
import com.fuel4media.carrythistoo.model.request.Establisment;
import com.fuel4media.carrythistoo.model.request.GunZone;
import com.fuel4media.carrythistoo.model.request.GunZoneRequest;
import com.fuel4media.carrythistoo.model.request.InviteOwners;
import com.fuel4media.carrythistoo.model.request.LoginRequest;
import com.fuel4media.carrythistoo.model.request.RepoMapRequest;
import com.fuel4media.carrythistoo.model.response.CalendarResponse;
import com.fuel4media.carrythistoo.model.response.EventList;
import com.fuel4media.carrythistoo.model.response.GunFreeZone;
import com.fuel4media.carrythistoo.model.response.LoginResponse;
import com.fuel4media.carrythistoo.model.response.PaymentTokenResponse;
import com.fuel4media.carrythistoo.model.response.RepoMapResponse;
import com.fuel4media.carrythistoo.model.response.StateLawsListing;
import com.fuel4media.carrythistoo.model.response.UserResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface DayZeroRestApiStore {


    @POST("user/login")
    @Headers("No-Authentication:true")
    Call<ResponseBean<LoginResponse>> performLogin(@Body LoginRequest loginRequest);

    @POST("user/register")
    @Headers("No-Authentication:true")
    Call<ResponseBean<LoginResponse>> performRegister(@Body LoginRequest registerRequest);

    @POST("user/verify")
    @Headers("No-Authentication:true")
    Call<ResponseBean<UserResponse>> performVerifyOTP(@Body LoginRequest verifyOTPRequest);

    @POST("user/settings")
    Call<ResponseBean> updateSettings(@Body Settings settings);

    @POST("updatecurrentlatlong")
    @FormUrlEncoded
    Call<ResponseBean> updateLatLong(@Field("lat") Double lat, @Field("long") Double lon);

    @POST("user/resend")
    @Headers("No-Authentication:true")
    Call<ResponseBean<LoginResponse>> resendOTP(@Body LoginRequest resendRequest);

    @POST("addevent")
    Call<ResponseBean<LoginResponse>> addEvent(@Body Event addEvent);

    @POST("editevent")
    Call<ResponseBean<LoginResponse>> editEvent(@Query("id") String id, @Body Event editEvent);

    @POST("deleteevent")
    Call<ResponseBean<LoginResponse>> deleteEvent(@Query("id") String id);

    @POST("listpermit")
    Call<ResponseBean<ArrayList<Permit>>> permitList();

    @POST("addpermit")
    @Multipart
    Call<ResponseBean> addPermit(@Part("state_name") RequestBody stateName, @Part("permit_id") RequestBody permitID, @Part("permit_type") RequestBody permitType, @Part MultipartBody.Part permit_image);

    @POST("editpermit")
    @Multipart
    Call<ResponseBean> editPermit(@Query("id") String id, @Part("state_name") RequestBody stateName, @Part("permit_id") RequestBody permitID, @Part("permit_type") RequestBody permitType, @Part MultipartBody.Part permit_image);

    @POST("deletepermit")
    Call<ResponseBean> deletePermit(@Query("id") String id);

    @POST("calendarlist")
    Call<ResponseBean<CalendarResponse>> calendarList(@Body CalendarRequest calendarRequest);

    @POST("eventlist")
    Call<ResponseBean<EventList>> eventList(@Body CalendarRequest calendarRequest);

    @POST("addfreegunzones")
    Call<ResponseBean> addEstablisment(@Body Establisment establisment);

    @POST("invitegunowner")
    Call<ResponseBean> inviteOwners(@Body InviteOwners inviteOwners);

    @FormUrlEncoded
    @POST("ccwstatelist")
    Call<ResponseBean<StateLawsListing>> stateLawList(@Field("state") String stateId);

    @FormUrlEncoded
    @POST("ccwscompaire")
    Call<ResponseBean<ArrayList<StateLaws>>> stateCompareLawsList(@Field("state1") String state1, @Field("state2") String state2);

    @POST("filtergunzones")
    Call<ResponseBean<ArrayList<GunZone>>> filterGunZone(@Body GunZoneRequest gunZoneRequest);

    @POST("notificationlist")
    @FormUrlEncoded
    Call<ResponseBean<ArrayList<GunFreeZone>>> getGunFreeZone(@Field("lat") Double lat, @Field("long") Double lon);


    @POST("sendmsg")
    Call<ResponseBean> sendMessage(@Body Message message);

    @POST("getmsg")
    Call<ResponseBean<ArrayList<Message>>> getMessages();

    @POST("suggestion")
    @FormUrlEncoded
    Call<ResponseBean> addSuggestion(@Field("suggestion") String suggestion);

    @POST("clienttoken")
    Call<ResponseBean<PaymentTokenResponse>> getPaymentToken();

    @POST("paymentcancel")
    Call<ResponseBean> cancelPaymentPlan();

    @POST("startpayment")
    Call<ResponseBean> startPremiumPlan();

    @POST("paymentrecieve")
    @FormUrlEncoded
    Call<ResponseBean> sendPaymentNonce(@Field("amount") float amount, @Field("nonce") String nonce);

    @POST("repomap")
    Call<ResponseBean<RepoMapResponse>> getRepoMap(@Body RepoMapRequest repoMapRequest);

    @FormUrlEncoded
    @POST("tokenupdate")
    Call<ResponseBean> updateFirebaseToken(@Field("device_id") String deviceID, @Field("device_token") String deviceToken);

    @POST("user/logout")
    Call<ResponseBean> logout();


}