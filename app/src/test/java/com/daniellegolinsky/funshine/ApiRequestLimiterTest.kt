package com.daniellegolinsky.funshine

import com.daniellegolinsky.funshine.data.ApiRequestLimiter
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.testImplementations.MockWeatherSettingsDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ApiRequestLimiterTest {

    private val mockDataStore = MockWeatherSettingsDataStore()
    private val apiRequestLimiter = ApiRequestLimiter(dataStore = mockDataStore)

    @Test
    fun shouldBlockNewCalls() = runTest {
        mockDataStore.setApiCallCount(ApiRequestLimiter.MAX_DAILY_REQUESTS)
        mockDataStore.setNewApiTimestamp(ForecastTimestamp.getCurrentTimestamp())
        assertFalse(apiRequestLimiter.requestPermitted())
    }

    @Test
    fun shouldNotBlockNewCalls() = runTest {
        mockDataStore.setApiCallCount(0)
        mockDataStore.setNewApiTimestamp(ForecastTimestamp.getCurrentTimestamp())
        assert(apiRequestLimiter.requestPermitted())
    }

    @Test
    fun atApiLimitButOlderThanOneDay() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        val newYear = if (today.day == 1) {
            today.year - 1
        } else {
            today.year
        }
        val newDay = if (today.day == 1) {
            31
        } else {
            today.day - 1
        }
        val yesterday = ForecastTimestamp(year = newYear, day = newDay, hour = today.hour - 1)
        mockDataStore.setApiCallCount(ApiRequestLimiter.MAX_DAILY_REQUESTS)
        mockDataStore.setNewApiTimestamp(today)
        // If the request comes in today, it should fail
        assertFalse(apiRequestLimiter.requestPermitted())
        mockDataStore.setNewApiTimestamp(yesterday)
        // Reset and test again
        assert(apiRequestLimiter.requestPermitted())
    }

    @Test
    fun testApiLimitDoesNotGetResetForToday() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        mockDataStore.setApiCallCount(ApiRequestLimiter.MAX_DAILY_REQUESTS)
        mockDataStore.setNewApiTimestamp(today)
        assertFalse(apiRequestLimiter.requestPermitted())
    }

    @Test
    fun testApiCountIncrementing() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        mockDataStore.setApiCallCount(ApiRequestLimiter.MAX_DAILY_REQUESTS - 1)
        mockDataStore.setNewApiTimestamp(today)
        assert(apiRequestLimiter.requestPermitted())
        apiRequestLimiter.incrementApiCallCounter()
        assertFalse(apiRequestLimiter.requestPermitted())
    }

    @Test
    fun testHoursLeft() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        val yesterday = today.copy(day = today.day - 1)
        mockDataStore.setNewApiTimestamp(today)
        assertEquals(ForecastTimestamp.HOURS_IN_DAY, apiRequestLimiter.hoursLeft())
        mockDataStore.setNewApiTimestamp(yesterday)
        assertEquals(0, apiRequestLimiter.hoursLeft())
    }
}
