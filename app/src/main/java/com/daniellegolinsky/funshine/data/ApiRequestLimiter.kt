package com.daniellegolinsky.funshine.data

import android.util.Log
import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.hoursBetween
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

class ApiRequestLimiter @Inject constructor(
    private val dataStore: IWeatherSettingsDataStore,
) : IApiRequestLimiter {

    companion object{
        // Increased to 1/hour for current install base
        // Enables all-day usage too, for now.
        const val MAX_DAILY_REQUESTS = 24
    }

    override suspend fun incrementApiCallCounter() {
        dataStore.incrementApiCallCount()
    }

    /**
     * Will reset the call counter if it can
     * Then checks if the current request is valid
     * Then increments the counter if a request will be made
     */
    override suspend fun requestPermitted(): Boolean {
        resetApiCallCounterAndTimestampIfValid()
        val currentRequestCount = dataStore.getApiCallCount()
        return currentRequestCount < MAX_DAILY_REQUESTS
    }

    /**
     * Mostly used for error messaging, passes on the number of hours until the next reset
     */
    override suspend fun hoursLeft(): Int {
        val storedFutureDate = dataStore.getApiTimestamp()?.tomorrow()
            ?: ForecastTimestamp.getCurrentTimestamp().tomorrow() // Fudge response to 24 hours if null
        val now = ForecastTimestamp.getCurrentTimestamp()
        var hoursBetween = now.hoursBetween(storedFutureDate)
        // In the rare chance of issue, force it to 1 day
        if (hoursBetween > 24) {
            Log.e("HOURS_LEFT_BUG", "Hours left in hoursLeft() method was: $hoursBetween")
            hoursBetween = ForecastTimestamp.HOURS_IN_DAY
        }
        return hoursBetween
    }

    /**
     * This will set the reset time to now, only called privately if check passed
     */
    private suspend fun setResetTimeNow() {
        val now = ForecastTimestamp.getCurrentTimestamp()
        dataStore.setNewApiTimestamp(now)
    }

    private suspend fun canPerformReset(): Boolean {
        val storedTimestamp = dataStore.getApiTimestamp() // May be null, which would trigger reset
        val currentTimestamp = ForecastTimestamp.getCurrentTimestamp()
        var aDayHasPassed = true
        storedTimestamp?.let{
            aDayHasPassed = it.hoursBetween(currentTimestamp) >= ForecastTimestamp.HOURS_IN_DAY
        }
        return aDayHasPassed
    }

    /**
     * Checks to see if the required time (currently one day) has passed
     * Then, if it has, resets the timestamp to now and resets the API counter
     * Don't call this directly, instead call requestPermitted()
     */
    private suspend fun resetApiCallCounterAndTimestampIfValid() {
        if (canPerformReset()) {
            dataStore.resetApiCallCount()
            setResetTimeNow()
        }
    }
}
