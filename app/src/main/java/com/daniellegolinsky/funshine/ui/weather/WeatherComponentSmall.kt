package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState

@Composable
fun WeatherComponentSmall(
    viewState: ViewState.Success<WeatherScreenViewState>,
    modifier: Modifier = Modifier,
) {
    val data = viewState.data
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(start = 0.dp, end = 2.dp, top = 8.dp, bottom = 4.dp)
            .fillMaxWidth()
    ) {
        ConditionsComponent(
            weatherIconResource = data.weatherIconResource,
            weatherIconContentDescription = data.weatherIconContentDescription,
            temperature = data.temperature,
            temperatureUnit = data.temperatureUnit,
            modifier = Modifier
                .fillMaxWidth(0.35f)
        )
        ForecastComponent(
            forecast = data.forecast,
            modifier = Modifier.fillMaxWidth(0.65f)
        )
    }
}

@Preview
@Composable
fun PreviewWeatherComponentSmall() {
    WeatherComponentSmall(viewState = ViewState.Success(
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
