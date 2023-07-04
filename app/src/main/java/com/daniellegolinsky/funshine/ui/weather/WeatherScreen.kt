package com.daniellegolinsky.funshine.ui.weather

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.designsystem.*
import com.daniellegolinsky.designsystem.components.FsButton
import com.daniellegolinsky.designsystem.components.FsText
import com.daniellegolinsky.designsystem.components.WeatherStatusImage
import com.daniellegolinsky.designsystem.font.getBodyFontStyle
import com.daniellegolinsky.designsystem.font.getHeadingFontStyle
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState

@Composable
fun WeatherScreen(
    viewState: WeatherScreenViewState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(32.dp) // TODO Obviously bad
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        WeatherStatusImage(
            imageResource = R.drawable.ic_sunny_black,
            imageResourceContentDescription = R.string.ic_sunny_content_description,
        )
        FsText(
            text = "${viewState.temperature}ºF", // TODO Make this a string resource
            textStyle = getHeadingFontStyle(),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        FsText(
            text = viewState.forecast,
            textStyle = getBodyFontStyle(),
            maxLines = 8
        )
        Spacer(modifier = Modifier.weight(1f))
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
                ){
            FsButton(
                buttonIcon = R.drawable.ic_settings_button_black,
                buttonIconContentDescription = R.string.ic_settings_button_content_description,
                onClick = { Toast.makeText(context, "What's wrong with my settings?", Toast.LENGTH_SHORT).show() }
            )
            Spacer(modifier = Modifier.width(2.dp))
            FsButton(
                buttonIcon = R.drawable.ic_refresh_button_black,
                buttonIconContentDescription = R.string.ic_refresh_button_content_description,
                onClick = { Toast.makeText(context, "How refreshing!", Toast.LENGTH_SHORT).show() }
            )
        }
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