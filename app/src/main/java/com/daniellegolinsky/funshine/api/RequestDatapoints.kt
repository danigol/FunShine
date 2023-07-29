package com.daniellegolinsky.funshine.api

import java.util.SimpleTimeZone

/**
 * Example Request:
 * https://api.open-meteo.com/v1/forecast?latitude=43.73&longitude=-73.99&hourly=temperature_2m,precipitation_probability,weathercode,cloudcover&daily=weathercode,temperature_2m_max,temperature_2m_min&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York
 */

class RequestDatapoints() {

    companion object {
        const val FORECAST: String = "v1/forecast"
        const val CURRENT: String = "current_weather"
        const val HOURLY: String = "hourly"
        const val DAILY: String = "daily"
        const val FORECAST_DAYS: String = "forecast_days"
        const val LAT: String = "latitude"
        const val LONG: String = "longitude"
        const val TEMPERATURE: String = "temperature_2m"
        const val TEMP_MAX: String = "${TEMPERATURE}_max"
        const val TEMP_MIN: String = "${TEMPERATURE}_min"
        const val PRECIP_PROB: String = "precipitation_probability"
        const val PRECIP_SUM: String = "precipitation_sum"
        const val PRECIP_PROB_MAX: String = "precipitation_probability_max"
        const val WEATHER_CODE: String = "weathercode"
        const val CLOUD_COVRE: String = "cloudcover"
        const val TEMP_UNIT: String = "temperature_unit"
        const val F: String = "fahrenheit"
        const val C: String = "celsius"
        const val WINDSPEED_UNIT: String = "windspeed_unit"
        const val MPH: String = "mph"
        const val KPH: String = "kph"
        const val PRECIPITATION_UNIT: String = "precipitation_unit"
        const val INCH: String = "inch"
        const val CM: String = "centimeter"
        const val TIME_ZONE: String = "timezone"
        const val US_NYC: String =
            "America%2FNew_York" // TODO: This seems like a problem, might be better to assume local time zone from location and get UTC and subtract
    }
}
