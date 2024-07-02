package com.example.weatherapp.Server

import com.example.weatherapp.model.CityResponseApi
import com.example.weatherapp.model.CurrentResponseApi
import com.example.weatherapp.model.ForecastReponseApi
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

    @GET("data/2.5/forecast")

    fun getForecastWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units: String,
        @Query("Appid") ApiKey: String
    ):retrofit2.Call<ForecastReponseApi>

    @GET("geo/1.0/direct")

    fun getCitiesList(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("Appid") ApiKey: String
    ): retrofit2.Call<CityResponseApi>
}