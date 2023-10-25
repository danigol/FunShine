package com.daniellegolinsky.funshine.data

interface IApiRequestLimiter {
    suspend fun incrementApiCallCounter()
    suspend fun resetApiCallCounterAndTimestampIfValid()
    suspend fun canMakeRequest(): Boolean
    suspend fun hoursLeft(): Int
}
