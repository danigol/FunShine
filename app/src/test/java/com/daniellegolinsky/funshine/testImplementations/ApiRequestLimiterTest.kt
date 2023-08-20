package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.data.ApiRequestLimiter
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ApiRequestLimiterTest {

    val mockDataStore = MockWeatherSettingsDataStore()
    val apiRequestLimiter = ApiRequestLimiter(dataStore = mockDataStore)

    @Test
    fun shouldBlockNewCalls() = runTest {
        mockDataStore.setApiCallCount(20)
        mockDataStore.setNewApiTimestamp(ForecastTimestamp.getCurrentTimestamp())
        assertFalse(apiRequestLimiter.canMakeRequest())
    }

    @Test
    fun shouldNotBlockNewCalls() = runTest {
        mockDataStore.setApiCallCount(0)
        mockDataStore.setNewApiTimestamp(ForecastTimestamp.getCurrentTimestamp())
        assert(apiRequestLimiter.canMakeRequest())
    }

    @Test
    fun atApiLimitButOlderThanOneDay() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        val yesterday = today.copy(day = today.day - 1, hour = today.hour - 1)
        mockDataStore.setApiCallCount(20)
        mockDataStore.setNewApiTimestamp(yesterday)
        // First make sure that, without reset, this fails
        assertFalse(apiRequestLimiter.canMakeRequest())
        // Reset and test again
        apiRequestLimiter.resetApiCallCounterAndTimestampIfValid()
        assert(apiRequestLimiter.canMakeRequest())
    }

    @Test
    fun testApiLimitDoesNotGetResetForToday() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        mockDataStore.setApiCallCount(20)
        mockDataStore.setNewApiTimestamp(today)
        // Should not be able to reset because timestamp is from today
        apiRequestLimiter.resetApiCallCounterAndTimestampIfValid()
        assertFalse(apiRequestLimiter.canMakeRequest())
    }

    @Test
    fun testApiCountIncrementing() = runTest {
        val today = ForecastTimestamp.getCurrentTimestamp()
        mockDataStore.setApiCallCount(ApiRequestLimiter.MAX_DAILY_REQUESTS - 1)
        mockDataStore.setNewApiTimestamp(today)
        assert(apiRequestLimiter.canMakeRequest())
        apiRequestLimiter.incrementApiCallCounter()
        assertFalse(apiRequestLimiter.canMakeRequest())
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
