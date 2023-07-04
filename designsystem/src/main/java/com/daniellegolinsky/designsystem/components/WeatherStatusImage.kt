package com.daniellegolinsky.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.designelements.getForegroundItemColor
import com.daniellegolinsky.designsystem.designelements.getShadowAlpha
import com.daniellegolinsky.designsystem.designelements.getShadowBlurRadius
import com.daniellegolinsky.designsystem.designelements.getShadowXOffset
import com.daniellegolinsky.designsystem.designelements.getShadowYOffset

@Composable
fun WeatherStatusImage(
    imageResource: Int,
    imageResourceContentDescription: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .alpha(alpha = getShadowAlpha())
                .offset(x = getShadowXOffset(), y = getShadowYOffset())
                .blur(radius = getShadowBlurRadius())
                .padding(maxOf(getShadowXOffset(), getShadowYOffset())),
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sunny_black),
            contentDescription = stringResource(imageResourceContentDescription),
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(maxOf(getShadowXOffset(), getShadowYOffset())),
        )
    }
}

@Preview
@Composable
fun PreviewWeatherStatusImage() {
    WeatherStatusImage(
        imageResource = R.drawable.ic_sunny_black,
        imageResourceContentDescription = R.string.ic_sunny_content_description
    )
}