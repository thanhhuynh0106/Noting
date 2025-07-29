package com.example.app.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/api/weather/get")
    Call<ResponseBody> getWeather(@Query("city") String city);
}
