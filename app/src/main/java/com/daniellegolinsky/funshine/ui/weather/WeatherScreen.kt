package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniellegolinsky.designsystem.*
import com.daniellegolinsky.designsystem.components.FsText
import com.daniellegolinsky.designsystem.components.WeatherStatusImage
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState

@Composable
fun WeatherScreen(
    viewState: WeatherScreenViewState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(32.dp) // TODO Obviously bad
    ) {
        WeatherStatusImage(
            imageResource = R.drawable.ic_sunny_black,
            imageResourceContentDescription = R.string.ic_sunny_content_description,
        )
        FsText(
            text = "${viewState.temperature}ºF", // TODO Make this a string resource
            fontSize = 28.sp,
            fontWeight = 600,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        FsText(
            text = viewState.forecast,
            fontSize = 16.sp,
            maxLines = 8
        )
    }
}

@Preview
@Composable
fun PreviewWeatherScreen() {
    WeatherScreen(
        WeatherScreenViewState(
            0,
            78,
            "It's going to be nice, all day, forever, just super nice."
        )
    )
}