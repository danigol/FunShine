package com.daniellegolinsky.funshine

import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.toWeatherCode
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WeatherConditionTest {

    @Test
    fun testIntToConditionExtension() = runTest {
        val conditionMap: Map<Int, WeatherCode> = mapOf(
            0 to WeatherCode.CLEAR,
            1 to WeatherCode.MOSTLY_CLEAR,
            2 to WeatherCode.PARTLY_CLOUDY,
            3 to WeatherCode.OVERCAST,
            63 to WeatherCode.RAIN,
            73 to WeatherCode.SNOW,
            95 to WeatherCode.THUNDERSTORM,
            123 to WeatherCode.UNKNOWN_CONDITION
        )
        for (condition in conditionMap.keys) {
            assertEquals(conditionMap[condition], condition.toWeatherCode())
        }
    }
}