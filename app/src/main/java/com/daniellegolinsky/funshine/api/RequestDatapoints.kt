package com.daniellegolinsky.funshine.api

import java.util.SimpleTimeZone

/**
 * Example Request:
 * https://api.open-meteo.com/v1/forecast?latitude=43.73&longitude=-73.99&hourly=temperature_2m,precipitation_probability,weathercode,cloudcover&daily=weathercode,temperature_2m_max,temperature_2m_min&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York
 */

data class RequestDatapoints(
    val current: String = "current_weather",
    val hourly: String = "hourly",
    val daily: String = "daily",
    val lat: String = "latitude",
    val long: String = "longitude",
    val temperature: String = "temperature_2m",
    val tempMax: String = "${temperature}_max",
    val tempMin: String = "${temperature}_min",
    val precip_prob: String = "precipitation_probability",
    val weatherCode: String = "weathercode",
    val cloudCover: String = "cloudcover",
    val tempUnit: String = "temperature_unit",
    val f: String = "fahrenheit",
    val c: String = "celsius",
    val windspeedUnit: String = "windspeed_unit",
    val mph: String = "mph",
    val kph: String = "kph",
    val precipitationUnit: String = "precipitation_unit",
    val inch: String = "inch",
    val cm: String = "centimeter",
    val timeZone: String = "timezone",
    val usNyc: String = "America New_York", // TODO: This seems like a problem, might be better to assume local time zone from location and get UTC and subtract
)
