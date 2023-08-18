package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.models.ForecastTimestamp

interface IApiRequestLimiter {
    suspend fun incrementApiCallCounter()
    suspend fun resetApiCallCounterAndTimestampIfValid()
    suspend fun canMakeRequest(): Boolean
    suspend fun hoursLeft(): Int
}