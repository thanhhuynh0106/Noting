package com.example.app.api;

import com.example.app.model.CaptchaResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CaptchaAPI {
    @GET("/api/captcha")
    Call<CaptchaResponse> getCaptcha();
}
