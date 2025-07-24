package com.example.app.api;

import com.example.app.model.LoginRequest;
import com.example.app.model.RegisterRequest;
import com.example.app.model.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/api/auth/login")
    Call<ResponseDTO> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/logout")
    Call<ResponseDTO> logout(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<ResponseDTO> register(@Body RegisterRequest registerRequest);
}
