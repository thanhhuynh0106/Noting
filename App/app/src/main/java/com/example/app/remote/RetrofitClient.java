package com.example.app.remote;

import android.content.Context;

import com.example.app.api.AuthAPI;
import com.example.app.api.CaptchaAPI;
import com.example.app.api.WeatherAPI;

import java.net.CookieHandler;
import java.net.CookieManager;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            CookieHandler cookieHandler = new CookieManager();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    .addInterceptor(new AuthInterceptor(context))
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static CaptchaAPI getCaptchaAPI(Context context) {
        return getClient(context).create(CaptchaAPI.class);
    }

    public static AuthAPI getAuthAPI(Context context) {
        return getClient(context).create(AuthAPI.class);
    }

    public static WeatherAPI getWeatherAPI(Context context) {
        return getClient(context).create(WeatherAPI.class);
    }

}
