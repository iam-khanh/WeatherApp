package com.example.weatherapp.Repository

import com.example.weatherapp.Server.ApiServices

class WeatherRepository (val api: ApiServices) {

    fun getCurrentWeather(lat: Double, lng:Double, unit: String ) =
        api.getCurrentWeather(lat, lng, unit, "b3a6d210e985856b1c21dd00eba1b12f")
}