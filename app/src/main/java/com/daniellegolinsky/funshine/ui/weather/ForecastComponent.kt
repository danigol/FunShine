package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshine.ui.components.ScrollIndicatorDots
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

@Composable
fun ForecastComponent(
    forecast: String,
    isOnSmallDisplay: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState: ScrollState? = if (isOnSmallDisplay) {
        rememberScrollState()
    } else {
        null
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (isOnSmallDisplay) {
            modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .fillMaxSize()
        } else {
            modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 12.dp)
        },
    ) {
        FsText(
            text = forecast,
            textStyle = getBodyFontStyle(),
            maxLines = 25,
            modifier = if (isOnSmallDisplay) {
                Modifier.weight(1f).verticalScroll(scrollState!!)
            } else {
                Modifier
            }
        )
        if (isOnSmallDisplay) {
            if (scrollState != null
                && !scrollState.isScrollInProgress
                && scrollState.value != scrollState.maxValue) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ScrollIndicatorDots()
                }
            }
        }
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
