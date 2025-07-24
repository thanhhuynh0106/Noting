package com.example.app.remote;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SharedPrefManager sharedPrefManager;

    public AuthInterceptor(Context context) {
        this.sharedPrefManager = SharedPrefManager.getInstance(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = sharedPrefManager.getToken();

        if (token == null || token.isEmpty()) {
            return chain.proceed(originalRequest);
        }

        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token);

        Request newRequest = builder.build();

        return chain.proceed(newRequest);
    }
}
