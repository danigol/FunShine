package com.daniellegolinsky.funshine.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

@Composable
fun ScrollIndicatorDots(
    modifier: Modifier = Modifier,
) {
    FsText(
        text = stringResource(R.string.scroll_indicator),
        textStyle = getBodyFontStyle(),
        modifier = modifier,
    )
}

@Preview
@Composable
fun PreviewScrollIndicatorDots() {
    ScrollIndicatorDots()
}
