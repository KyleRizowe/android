/*
package com.fuel4media.carrythistoo.network;


import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



class HttpInterceptor implements Interceptor {

    private static final String TAG = HttpInterceptor.class.getSimpleName();
    private final OkHttpClient httpClient;

    HttpInterceptor(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //Build new request
        Request.Builder builder = request.newBuilder();
        builder.header("Content-Type", "application/json");

        String token = Prefs.instanceOf().getToken(); //save token of this request for future
        setAuthHeader(builder, token); //write current token to request

        request = builder.build(); //overwrite old request
        Response response = chain.proceed(request); //perform request, here original request will be executed

        if (response.code() == 401) { //if unauthorized
            synchronized (httpClient) { //perform all 401 in sync blocks, to avoid multiply token updates
               // logout();               //go to login screen
            }
        }

        try {
            //commented intentionally as can be used later.
//            printLogs(chain, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    */
/**
     * This method can be used to log http request and response.
     * @param chain
     * @param request
     * @param response
     *//*

    private void printLogs(Chain chain, Request request, Response response) {
        long t1 = System.nanoTime();
        Logger.d(TAG, String.format("Sending request %s on %s%n%s%n%s",
                request.url(), chain.connection(), request.headers(), request.body()));

        long t2 = System.nanoTime();
        Logger.d(TAG, String.format(Locale.getDefault(),"Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header(HEADER_PARAM_KEY_TOKEN, Prefs.instanceOf().getToken());
    }



}*/
