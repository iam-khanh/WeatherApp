package com.example.weatherapp.Server

import com.example.weatherapp.model.CurrentResponseApi
import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("data/2.5/weather")

    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units: String,
        @Query("Appid") ApiKey: String
    ):retrofit2.Call<CurrentResponseApi>
}