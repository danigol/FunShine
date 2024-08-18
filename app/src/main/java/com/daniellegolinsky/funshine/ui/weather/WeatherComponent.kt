package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import com.daniellegolinsky.themeresources.WeatherIconConstants

@Composable
fun WeatherComponent(
    viewState: ViewState.Success<WeatherScreenViewState>,
    modifier: Modifier = Modifier,
) {
    val data = viewState.data
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(32.dp)
    ) {
        ConditionsComponent(
            weatherIconResource = data.weatherIconResource,
            weatherIconContentDescription = data.weatherIconContentDescription,
            weatherIconSize = WeatherIconConstants.SIZE,
            temperature = data.temperature,
            temperatureUnit = data.temperatureUnit
        )
        ForecastComponent(forecast = data.forecast)
    }
}

@Preview
@Composable
fun PreviewWeatherComponent() {
    WeatherComponent(viewState = ViewState.Success(
        WeatherScreenViewState(
            weatherIconResource = com.daniellegolinsky.themeresources.R.drawable.ic_sunny_black,
            weatherIconContentDescription = com.daniellegolinsky.themeresources.R.string.ic_sunny_content_description,
            temperature = 74,
            temperatureUnit = "ºF",
            windspeedUnit = "MPH",
            precipitationAmountUnit = "in",
            forecast = "It's gonna be great all day. Absolutely perfect.",
            buttonsOnRight = true,
        )
    ))
}
