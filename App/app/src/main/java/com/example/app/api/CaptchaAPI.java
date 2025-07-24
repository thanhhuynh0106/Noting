package com.example.app.api;

import com.example.app.model.CaptchaResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CaptchaAPI {
    @GET("/api/captcha")
    Call<CaptchaResponse> getCaptcha();

    @POST("api/orc/solve")
    Call<Map<String, String>> solveCaptcha(@Body Map<String, String> requestBody);
}
