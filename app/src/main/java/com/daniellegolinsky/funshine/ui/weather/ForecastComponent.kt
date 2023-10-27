package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

@Composable
fun ForecastComponent(
    forecast: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        FsText(
            text = forecast,
            textStyle = getBodyFontStyle(),
            maxLines = 15,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun PreviewForecastComponent() {
    ForecastComponent(
        forecast = "It's gonna be great all day. Absolutely perfect.",
    )
}