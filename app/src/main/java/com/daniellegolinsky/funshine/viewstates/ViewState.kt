package com.daniellegolinsky.funshine.viewstates

sealed class ViewState<T>(val viewState: T) {
    data class Success<T>(val vs: T) : ViewState<T>(viewState = vs)
    data class Error<T>(val vs: T) : ViewState<T>(viewState = vs)
    class Loading<T>(val vs: T) : ViewState<T>(viewState = vs)
}
