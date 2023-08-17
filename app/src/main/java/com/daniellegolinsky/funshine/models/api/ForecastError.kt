package com.daniellegolinsky.funshine.models.api

import com.google.gson.annotations.SerializedName

/**
 * When things go wrong, this pulls what error we can out of the response
 */
data class ForecastError(
    @SerializedName("error")
    val isError: Boolean,
    @SerializedName("reason")
    val errorMessage: String,
)
