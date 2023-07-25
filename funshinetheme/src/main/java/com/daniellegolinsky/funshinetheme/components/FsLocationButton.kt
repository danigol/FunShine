package com.daniellegolinsky.funshinetheme.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.R

/**
 * A simple FsIconButton wrapper that includes the icon and content description automatically
 */
@Composable
fun FsLocationButton(
    modifier: Modifier = Modifier, 
    onClick: () -> Unit
) {
    FsIconButton(
        buttonIcon = painterResource(id = R.drawable.ic_button_precise_location),
        buttonIconContentDescription = stringResource(id = R.string.location_button_content_description),
        modifier = modifier,
        onClick = onClick
    )
}

@Preview
@Composable
fun PreviewFsLocationButton() {
    FsLocationButton {
    }
}