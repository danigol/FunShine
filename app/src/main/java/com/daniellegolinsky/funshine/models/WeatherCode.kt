package com.daniellegolinsky.funshine.models

/**
 * WMO Weather interpretation codes (WW)
 *   Code 	        Description
 *   0  	        Clear sky
 *   1, 2, 3 	    Mainly clear, partly cloudy, and overcast
 *   45, 48 	    Fog and depositing rime fog
 *   51, 53, 55 	Drizzle: Light, moderate, and dense intensity
 *   56, 57 	    Freezing Drizzle: Light and dense intensity
 *   61, 63, 65 	Rain: Slight, moderate and heavy intensity
 *   66, 67 	    Freezing Rain: Light and heavy intensity
 *   71, 73, 75 	Snow fall: Slight, moderate, and heavy intensity
 *   77 	        Snow grains
 *   80, 81, 82 	Rain showers: Slight, moderate, and violent
 *   85, 86 	    Snow showers slight and heavy
 *   95 * 	        Thunderstorm: Slight or moderate
 *   96, 99 * 	    Thunderstorm with slight and heavy hail
 *   * Thunderstorm Hail is only available in central europe
 */

enum class WeatherCode(wcode: Int) {
    CLEAR(0),
    MOSTLY_CLEAR(1),
    PARTLY_CLOUDY(2),
    OVERCAST(3),
    FOG(45),
    FREEZING_FOG(48),
    LIGHT_DRIZZLE(51),
    DRIZZLE(53),
    HEAVY_DRIZZLE(55),
    LIGHT_FREEZING_DRIZZLE(56),
    FREEZING_DRIZZLE(57),
    LIGHT_RAIN(61),
    RAIN(63),
    HEAVY_RAIN(65),
    LIGHT_FREEZING_RAIN(66),
    FREEZING_RAIN(67),
    LIGHT_SNOW(71),
    SNOW(73),
    HEAVY_SNOW(75),
    LIGHT_RAIN_SHOWERS(80),
    RAIN_SHOWERS(81),
    VIOLENT_RAIN_SHOWERS(82),
    LIGHT_SNOW_SHOWERS(85),
    SNOW_SHOWERS(86),
    THUNDERSTORM(95),
    THUNDERSTORM_WITH_LIGHT_HAIL(96),
    THUNDERSTORM_WITH_HEAVY_HAIL(99),
}