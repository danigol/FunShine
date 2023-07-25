package com.daniellegolinsky.funshinetheme.components

import android.util.LayoutDirection
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.R

@Composable
fun FsBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FsIconButton(
        buttonIcon = if (LocalLayoutDirection.current.equals(LayoutDirection.RTL)) {
            painterResource(id = R.drawable.ic_arrow_right_black)
        } else {
            painterResource(id = R.drawable.ic_arrow_left_black)
        },
        buttonIconContentDescription = stringResource(id = R.string.location_button_content_description),
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
