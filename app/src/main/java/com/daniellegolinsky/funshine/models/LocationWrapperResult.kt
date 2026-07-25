package com.daniellegolinsky.funshine.models

sealed class LocationWrapperResult<T> {
    data class Success<T>(val location: T) : LocationWrapperResult<T>()
    data class Error<T>(val errorString: String) : LocationWrapperResult<T>()
    class Loading<T>() : LocationWrapperResult<T>()
}
