package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.components.FsIconWithShadow
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle

@Composable
fun ConditionsComponent(
    weatherIconResource: Int,
    weatherIconContentDescription: Int,
    temperature: Int,
    temperatureUnit: String,
    modifier: Modifier = Modifier,
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
            FsIconWithShadow(
                image = painterResource(weatherIconResource),
                imageResourceContentDescription = stringResource(id = weatherIconContentDescription),
            )
        }
        FsText(
            text = "${temperature}${temperatureUnit}",
            textStyle = getHeadingFontStyle(),
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun PreviewConditionsComponent() {
    ConditionsComponent(
        weatherIconResource = com.daniellegolinsky.themeresources.R.drawable.ic_sunny_black,
        weatherIconContentDescription = com.daniellegolinsky.themeresources.R.string.ic_sunny_content_description,
        temperature = 74,
        temperatureUnit = "ºF",
    )
}
