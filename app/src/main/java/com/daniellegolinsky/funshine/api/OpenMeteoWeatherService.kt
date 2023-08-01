package com.daniellegolinsky.funshine.api

import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.CURRENT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.DAILY
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.F
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.FORECAST
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.HOURLY
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.INCH
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LAT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LAT_PARAM
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LONG
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.LONG_PARAM
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.MPH
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.PRECIPITATION_UNIT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.PRECIP_PROB
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.PRECIP_PROB_MAX
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.PRECIP_SUM
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TEMPERATURE
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TEMP_MAX
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TEMP_MIN
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TEMP_UNIT
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.TIME_ZONE
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.US_NYC
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.WEATHER_CODE
import com.daniellegolinsky.funshine.api.RequestDatapoints.Companion.WINDSPEED_UNIT
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenMeteoWeatherService {

    // https://api.open-meteo.com/v1/forecast?latitude=40.73&longitude=-73.99&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York
    // https://api.open-meteo.com/v1/forecast?latitude=43.73&longitude=-73.99&hourly=temperature_2m,precipitation_probability,weathercode&daily=weathercode,temperature_2m_max,temperature_2m_min&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York&forecast_days=1
    // https://api.open-meteo.com/v1/forecast?latitude=43.73&longitude=-73.99&hourly=temperature_2m,precipitation_probability,weathercode&daily=weathercode,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York&forecast_days=1
    @GET("$FORECAST?$HOURLY=$TEMPERATURE,$PRECIP_PROB,$WEATHER_CODE&$DAILY=$WEATHER_CODE,$TEMP_MAX,$TEMP_MIN,$PRECIP_PROB_MAX,$PRECIP_SUM&$CURRENT=true&$WINDSPEED_UNIT=$MPH&$PRECIPITATION_UNIT=$INCH&$TIME_ZONE=$US_NYC")
    suspend fun getCurrentWeather(
        @Query(LAT)latitude: Float,
        @Query(LONG) longitude: Float,
        @Query(TEMP_UNIT) tempUnit: String,
    ): WeatherResponse // TODO Wrap in a response object so we can do error checking
}
