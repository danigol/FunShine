package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniellegolinsky.designsystem.*
import com.daniellegolinsky.designsystem.components.WeatherStatusImage
import com.daniellegolinsky.designsystem.designelements.getShadowAlpha
import com.daniellegolinsky.designsystem.designelements.getTextColor
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
//        Icon(
//            painter = painterResource(id = R.drawable.ic_sunny_black),
//            contentDescription = "",
//            modifier = Modifier.padding(32.dp)
//        )
//        Spacer(modifier = Modifier.height(16.dp)) // Okay, yeah, we'll standardize everything, okay?
        WeatherStatusImage(
            imageResource = R.drawable.ic_sunny_black,
            imageResourceContentDescription = R.string.ic_sunny_content_description,
        )
        Text(
            text = "${viewState.temperature}ºF", // Obviously we're mocking this out.
            fontSize = 28.sp, // TODO No, bad! :P Lazy dev!
            textAlign = TextAlign.Center,
            color = getTextColor(),
            style = MaterialTheme.typography.bodyLarge.copy(
                shadow = Shadow(
                    color = colorResource(id = R.color.black).copy(alpha = getShadowAlpha()),
                    offset = Offset(x = 40f, y = 40f),
                    blurRadius = 12f
                ),
                fontWeight = FontWeight(600)
            ),
            //modifier = Modifier.shadow(16.dp).fillMaxWidth().height(42.dp) // TODO Obviously we'll make a design system version
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            viewState.forecast,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = getTextColor(),
            maxLines = 8,
            style = MaterialTheme.typography.bodyLarge.copy(
                shadow = Shadow(
                    color = colorResource(id = R.color.black).copy(alpha = getShadowAlpha()),
                    offset = Offset(x = 40f, y = 40f),
                    blurRadius = 12f
                ),
                fontWeight = FontWeight(450)
            ),
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