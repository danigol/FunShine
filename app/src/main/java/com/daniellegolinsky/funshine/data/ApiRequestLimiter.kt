package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.hoursBetween
import javax.inject.Inject

class ApiRequestLimiter @Inject constructor(
    private val dataStore: WeatherSettingsDataStore,
) : IApiRequestLimiter {

    private val maxDailyRequests = 20

    override suspend fun incrementApiCallCounter() {
        dataStore.incrementApiCallCount()
    }

    /**
     * Checks to see if the required time (currently one day) has passed
     * Then, if it has, resets the timestamp to now and resets the API counter
     */
    override suspend fun resetApiCallCounterAndTimestampIfValid() {
        if (overOneDayBetween()) {
            dataStore.resetApiCallCount()
            setResetTimeNow()
        }
    }

    /**
     * Resets the timestamp and counter if necessary. We always want to ensure it's up to date
     * Then checks if the counter is under the limit.
     * Timestamp only matters for resetting the counter, not for limits.
     */
    override suspend fun canMakeRequest(): Boolean {
        // First reset if necessary
        resetApiCallCounterAndTimestampIfValid()
        // Now check count and return check
        val currentRequestCount = dataStore.getApiCallCount()
        return currentRequestCount < maxDailyRequests
    }

    /**
     * This will return the reset time, which is the current (or now) timestamp PLUS ONE DAY.
     * The returned value from this can be as much as 24 hours in the future, as it is the reset time
     */
    override suspend fun getOrSetResetTime(): ForecastTimestamp {
        val storedTimestamp = dataStore.getApiTimestamp()
        return storedTimestamp?.tomorrow() ?: setResetTimeNow().tomorrow()
    }

    /**
     * This will set the reset time to now and return the timestamp used to create the API timestamp
     */
    override suspend fun setResetTimeNow(): ForecastTimestamp {
        val now = ForecastTimestamp.getCurrentTimestamp()
        dataStore.setNewApiTimestamp(now)
        return now
    }

    override suspend fun hoursLeft(): Int {
        val tomorrowTimestamp = getOrSetResetTime() // Already gets the reset time (stamp +1d)
        val now = ForecastTimestamp.getCurrentTimestamp()
        return now.hoursBetween(tomorrowTimestamp)
    }

    private suspend fun overOneDayBetween(): Boolean {
        val storedTimestamp = getOrSetResetTime()
        val currentTimestamp = ForecastTimestamp.getCurrentTimestamp()
        var aDayHasPassed = false
        storedTimestamp?.let{
            aDayHasPassed = it.hoursBetween(currentTimestamp) > ForecastTimestamp.HOURS_IN_DAY
        }
        return aDayHasPassed
    }
}