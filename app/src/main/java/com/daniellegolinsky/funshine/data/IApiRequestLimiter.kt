package com.daniellegolinsky.funshine.data

interface IApiRequestLimiter {
    suspend fun incrementApiCallCounter()
    suspend fun requestPermitted(): Boolean
    suspend fun hoursLeft(): Int
}
