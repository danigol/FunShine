package com.daniellegolinsky.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.designelements.getForegroundItemColor
import com.daniellegolinsky.designsystem.designelements.getShadowAlpha

@Composable
fun WeatherStatusImage(
    imageResource: Int,
    imageResourceContentDescription: Int,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = imageResource),
            contentDescription = stringResource(imageResourceContentDescription),
            modifier = Modifier
                .alpha(alpha = getShadowAlpha())
                .offset(x = 28.dp, y = 28.dp)
                .blur(radius = 12.dp)
                .padding(40.dp),
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sunny_black),
            contentDescription = "",
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(40.dp)
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