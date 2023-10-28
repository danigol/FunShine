package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshinetheme.components.FsIconWithShadow
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            FsIconWithShadow(
                image = painterResource(com.daniellegolinsky.themeresources.R.drawable.ic_loading_black),
                imageResourceContentDescription = stringResource(id = R.string.wc_loading),
            )
        }
        FsText(
            text = stringResource(id = R.string.loading),
            textStyle = getHeadingFontStyle(),
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    LoadingScreen()
}