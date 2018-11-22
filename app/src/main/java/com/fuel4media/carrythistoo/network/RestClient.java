package com.fuel4media.carrythistoo.network;

import android.support.annotation.NonNull;

import com.fuel4media.carrythistoo.prefrences.AppPreference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kiwitech on 09/8/17.
 */

public class RestClient {

    private static RestClient ourInstance;
    private Retrofit retrofit;

    public static RestClient getInstance() {
        if (ourInstance == null) ourInstance = new RestClient();
        return ourInstance;
    }

    private RestClient() {
        createRetrofitInstance();
    }

    /**
     * Create Retrofit Instance
     */
    private void createRetrofitInstance() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstants.APP_SERVER_URL)
                .client(getHeaderBuilder().build())
                .build();
    }

    @NonNull
    private OkHttpClient.Builder getHeaderBuilder() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                if (original.header("No-Authentication") == null) {
                 /*   String base = "Bearer %s";
                    String token = AppPreference.getInstance().getSessionToken();
                    String token1 = token.substring(7, token.length());*/

                    Request request = original.newBuilder()
                            .addHeader("Cache-Control", "no-cache")
                            .addHeader(NetworkConstants.HEADER_PARAM_KEY_TOKEN, "Bearer " + AppPreference.getInstance().getSessionToken())
                            .addHeader(NetworkConstants.HEADER_PARAM_KEY_ACCEPT, NetworkConstants.HEADER_PARAM_VALUE_ACCEPT)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                } else {
                    return chain.proceed(original);
                }
            }
        });


        // Setting timeout
        httpClient.readTimeout(NetworkConstants.RETROFIT_API_SERVICE_SOCKET_TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(NetworkConstants.RETROFIT_API_SERVICE_SOCKET_TIMEOUT, TimeUnit.SECONDS);
        httpClient.followRedirects(true);
        httpClient.followSslRedirects(true);

        // httpClient.addNetworkInterceptor(httpClient.build());

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!


        return httpClient;
    }


    private Retrofit getRetrofit() {
        return retrofit;
    }


    DayZeroRestApiStore getRestAPIStore() {
        return getRetrofit().create(DayZeroRestApiStore.class);
    }


    public void destroy() {
        ourInstance = null;
    }

}
