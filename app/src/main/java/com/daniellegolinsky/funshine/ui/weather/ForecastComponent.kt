package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

@Composable
fun ForecastComponent(
    forecast: String,
    isOnSmallDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (isOnSmallDisplay) {
            modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .verticalScroll(rememberScrollState())
        } else {
            modifier
                .padding(top = 12.dp, bottom = 12.dp)
        },
    ) {
        FsText(
            text = forecast,
            textStyle = getBodyFontStyle(),
            maxLines = 25,
        )
    }
}

@Preview
@Composable
fun PreviewForecastComponent() {
    ForecastComponent(
        forecast = "It's gonna be great all day. Absolutely perfect.",
        isOnSmallDisplay = false,
    )
}

@Preview
@Composable
fun PreviewSmallForecastComponent() {
    ForecastComponent(
        forecast = "It's gonna be great all day. Absolutely perfect.",
        isOnSmallDisplay = true,
    )
}
