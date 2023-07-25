package com.daniellegolinsky.funshinetheme.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.daniellegolinsky.funshinetheme.R

/**
 * A back button with RTL support
 * Content Description will read "Back" Defined here: R.string.back_button_content_description
 */
@Composable
fun FsBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FsIconButton(
        buttonIcon = if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
            painterResource(id = R.drawable.ic_arrow_right_black)
        } else {
            painterResource(id = R.drawable.ic_arrow_left_black)
        },
        buttonIconContentDescription = stringResource(id = R.string.back_button_content_description),
        modifier = modifier,
        onClick = onClick
    )
}

@Preview
@Composable
fun PreviewFsBackButton() {
    FsBackButton {
    }
}
