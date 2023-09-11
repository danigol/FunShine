package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.R
import com.google.gson.annotations.SerializedName
import com.daniellegolinsky.themeresources.R.drawable
import kotlinx.serialization.Serializable

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
 *
 *   Maps ints to enum from response. Will keep int value so we can return a resource string
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

    UNKNOWN_CONDITION(-1),
}

fun Int.toWeatherCode(): WeatherCode {
    return when (this) {
        0 -> WeatherCode.CLEAR
        1 -> WeatherCode.MOSTLY_CLEAR
        2 -> WeatherCode.PARTLY_CLOUDY
        3 -> WeatherCode.OVERCAST
        45 -> WeatherCode.FOG
        48 -> WeatherCode.FREEZING_FOG
        51 -> WeatherCode.LIGHT_DRIZZLE
        53 -> WeatherCode.DRIZZLE
        55 -> WeatherCode.HEAVY_DRIZZLE
        56 -> WeatherCode.LIGHT_FREEZING_DRIZZLE
        57 -> WeatherCode.FREEZING_DRIZZLE
        61 -> WeatherCode.LIGHT_RAIN
        63 -> WeatherCode.RAIN
        65 -> WeatherCode.HEAVY_RAIN
        66 -> WeatherCode.LIGHT_FREEZING_RAIN
        67 -> WeatherCode.FREEZING_RAIN
        71-> WeatherCode.LIGHT_SNOW
        73 -> WeatherCode.SNOW
        75 -> WeatherCode.HEAVY_SNOW
        80 -> WeatherCode.LIGHT_RAIN_SHOWERS
        81 -> WeatherCode.RAIN_SHOWERS
        82 -> WeatherCode.VIOLENT_RAIN_SHOWERS
        85 -> WeatherCode.LIGHT_SNOW_SHOWERS
        86 -> WeatherCode.SNOW_SHOWERS
        95, 96, 99 -> WeatherCode.THUNDERSTORM
//        96 -> WeatherCode.THUNDERSTORM_WITH_LIGHT_HAIL // These tend to get over-used?
//        99 -> WeatherCode.THUNDERSTORM_WITH_HEAVY_HAIL // Not valid outside of EU, but show up in U.S. sometimes
        else -> WeatherCode.UNKNOWN_CONDITION
    }
}

fun WeatherCode.getResourceStringForWeatherCode(): Int {
    return when (this) {
        WeatherCode.CLEAR -> R.string.wc_clear
        WeatherCode.MOSTLY_CLEAR -> R.string.wc_mostly_clear
        WeatherCode.PARTLY_CLOUDY -> R.string.wc_partly_cloudy
        WeatherCode.OVERCAST -> R.string.wc_overcast
        WeatherCode.FOG -> R.string.wc_fog
        WeatherCode.FREEZING_FOG -> R.string.wc_freezing_fog
        WeatherCode.LIGHT_DRIZZLE -> R.string.wc_light_drizzle
        WeatherCode.DRIZZLE -> R.string.wc_drizzle
        WeatherCode.HEAVY_DRIZZLE -> R.string.wc_heavy_drizzle
        WeatherCode.LIGHT_FREEZING_DRIZZLE -> R.string.wc_light_freezing_drizzle
        WeatherCode.FREEZING_DRIZZLE -> R.string.wc_light_freezing_drizzle
        WeatherCode.LIGHT_RAIN -> R.string.wc_light_rain
        WeatherCode.RAIN -> R.string.wc_rain
        WeatherCode.HEAVY_RAIN -> R.string.wc_heavy_rain
        WeatherCode.LIGHT_FREEZING_RAIN -> R.string.wc_light_freezing_rain
        WeatherCode.FREEZING_RAIN -> R.string.wc_freezing_rain
        WeatherCode.LIGHT_SNOW -> R.string.wc_light_snow
        WeatherCode.SNOW -> R.string.wc_snow
        WeatherCode.HEAVY_SNOW -> R.string.wc_heavy_rain
        WeatherCode.LIGHT_RAIN_SHOWERS -> R.string.wc_light_rain_showers
        WeatherCode.RAIN_SHOWERS -> R.string.wc_rain_showers
        WeatherCode.VIOLENT_RAIN_SHOWERS -> R.string.wc_violent_rain_showers
        WeatherCode.LIGHT_SNOW_SHOWERS -> R.string.wc_light_snow_showers
        WeatherCode.SNOW_SHOWERS -> R.string.wc_snow_showers
        WeatherCode.THUNDERSTORM -> R.string.wc_thunderstorm
        WeatherCode.THUNDERSTORM_WITH_LIGHT_HAIL -> R.string.wc_thunderstorm_with_light_hail
        WeatherCode.THUNDERSTORM_WITH_HEAVY_HAIL -> R.string.wc_thunderstorm_with_heavy_hail
        else -> R.string.wc_unknown
    }
}

fun WeatherCode.getIconResource(isDay: Boolean = true): Int {
    return when (this) {
        WeatherCode.CLEAR -> if (isDay) {
            drawable.ic_sunny_black
        } else {
            drawable.ic_moon_clear_black
        }
        WeatherCode.MOSTLY_CLEAR -> if (isDay) {
            drawable.ic_partly_sunny_black
        } else {
            drawable.ic_partly_moony_black
        }
        WeatherCode.PARTLY_CLOUDY -> if (isDay) {
            drawable.ic_partly_cloudy_black
        } else {
            drawable.ic_partly_cloudy_night_black
        }

        WeatherCode.OVERCAST -> drawable.ic_cloudy_black

        WeatherCode.LIGHT_RAIN,
        WeatherCode.LIGHT_DRIZZLE,
        WeatherCode.DRIZZLE -> drawable.ic_drizzle_black

        WeatherCode.RAIN,
        WeatherCode.HEAVY_RAIN,
        WeatherCode.LIGHT_RAIN_SHOWERS,
        WeatherCode.RAIN_SHOWERS,
        WeatherCode.VIOLENT_RAIN_SHOWERS,
        WeatherCode.HEAVY_DRIZZLE -> drawable.ic_rain_black

        WeatherCode.LIGHT_SNOW,
        WeatherCode.SNOW,
        WeatherCode.HEAVY_SNOW,
        WeatherCode.LIGHT_SNOW_SHOWERS,
        WeatherCode.SNOW_SHOWERS -> drawable.ic_snowing_black

        WeatherCode.THUNDERSTORM,
        WeatherCode.THUNDERSTORM_WITH_LIGHT_HAIL,
        WeatherCode.THUNDERSTORM_WITH_HEAVY_HAIL -> drawable.ic_lightning_black // TODO, I mean, add more...

        WeatherCode.FOG,
        WeatherCode.FREEZING_FOG -> drawable.ic_fog_black

        WeatherCode.LIGHT_FREEZING_DRIZZLE,
        WeatherCode.FREEZING_DRIZZLE,
        WeatherCode.LIGHT_FREEZING_RAIN,
        WeatherCode.FREEZING_RAIN -> drawable.ic_freezing_rain // TODO Differentiate better

        else -> drawable.ic_circle_x_black
    }
}
