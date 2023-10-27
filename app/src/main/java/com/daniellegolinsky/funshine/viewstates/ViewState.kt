package com.daniellegolinsky.funshine.viewstates

sealed class ViewState<T> {
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error<T>(val errorString: String) : ViewState<T>()
    class Loading<T>() : ViewState<T>()
}
