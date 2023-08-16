package com.daniellegolinsky.funshine.models

data class ResponseOrError<T, U> (
    val isError: Boolean,
    val data: T? = null,
    val error: U? = null,
)
