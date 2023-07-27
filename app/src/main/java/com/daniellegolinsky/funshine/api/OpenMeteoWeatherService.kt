package com.daniellegolinsky.funshine.api

import com.daniellegolinsky.funshine.models.WeatherResponse
import retrofit2.http.GET

interface OpenMeteoWeatherService {

    // TODO Obviously not this, but okay
    @GET("v1/forecast?latitude=40.73&longitude=-73.99&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch")
    suspend fun getCurrentWeather(): WeatherResponse
}