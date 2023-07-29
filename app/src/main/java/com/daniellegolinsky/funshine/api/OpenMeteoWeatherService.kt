package com.daniellegolinsky.funshine.api

import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.CURRENT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.F
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.FORECAST
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.INCH
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LAT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LONG
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.MPH
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.PRECIPITATION_UNIT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TEMP_UNIT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TIME_ZONE
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.US_NYC
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.WINDSPEED_UNIT
import com.daniellegolinsky.funshine.models.WeatherResponse
import retrofit2.http.GET

interface OpenMeteoWeatherService {

    // TODO Obviously not this, but okay
    // https://api.open-meteo.com/v1/forecast?latitude=40.73&longitude=-73.99&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch
    @GET("$FORECAST?$LAT=40.73&$LONG=-73.99&$CURRENT=true&$TEMP_UNIT=$F&$WINDSPEED_UNIT=$MPH&$PRECIPITATION_UNIT=$INCH&$TIME_ZONE=$US_NYC")
    suspend fun getCurrentWeather(): WeatherResponse
}