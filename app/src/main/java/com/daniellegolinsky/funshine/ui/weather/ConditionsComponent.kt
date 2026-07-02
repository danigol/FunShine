package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.daniellegolinsky.funshine.ui.getShadowMatrix
import com.daniellegolinsky.funshinetheme.components.FsIconWithShadow
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.designelements.ThemeConstants
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle
import com.daniellegolinsky.themeresources.WeatherIconConstants

@Composable
fun ConditionsComponent(
    weatherIconResource: Int,
    weatherIconContentDescription: Int,
    weatherIconSize: Dp,
    temperature: Int,
    temperatureUnit: String,
    modifier: Modifier = Modifier,
    isOnSmallDisplay: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            // Small screens will use hint shadow to reduce padding
            FsIconWithShadow(
                image = painterResource(weatherIconResource),
                imageResourceContentDescription = stringResource(id = weatherIconContentDescription),
                size = weatherIconSize,
                modifier = Modifier.fillMaxWidth(),
                providedShadowMatrix = getShadowMatrix(isOnSmallDisplay)
            )
        }
        FsText(
            text = "${temperature}${temperatureUnit}",
            textStyle = if (isOnSmallDisplay) {
                getHeadingFontStyle(
                    shadowOffsetX = ThemeConstants.SHADOW_OFFSET_X_QUARTER,
                    shadowOffsetY = ThemeConstants.SHADOW_OFFSET_Y_QUARTER,
                )
            } else {
                getHeadingFontStyle()
            },
            maxLines = 1,
        )
    }
}

@Preview
@Composable
fun PreviewConditionsComponent() {
    ConditionsComponent(
        weatherIconResource = com.daniellegolinsky.themeresources.R.drawable.ic_sunny_black,
        weatherIconContentDescription = com.daniellegolinsky.themeresources.R.string.ic_sunny_content_description,
        weatherIconSize = WeatherIconConstants.SIZE,
        temperature = 74,
        temperatureUnit = "ºF",
    )
}
