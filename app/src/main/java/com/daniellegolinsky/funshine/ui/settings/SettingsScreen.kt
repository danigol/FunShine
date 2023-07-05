package com.daniellegolinsky.funshine.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.designsystem.components.FsIconButton
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    modifier: Modifier = Modifier
) {
    // TODO TODOs all around!
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Faux heading line/appbar (I always found them a bit ugly, but a back arrow is okay?)
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            FsIconButton(
                buttonIcon = R.drawable.ic_arrow_left_black,
                buttonIconContentDescription = R.string.ic_settings_button_back,
                onClick = {} // TODO Basic back
            )
            // TODO Do we want to just make an app bar with the title? Or is it obvious with just 2 screens?
        }
        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 64.dp, start = 32.dp, end = 32.dp, bottom = 64.dp)
        ) {
            TextField( // TODO Make a custom text field so it's consistent across Android versions?
                value = viewState.apiKey,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "${viewState.latitude}, ${viewState.longitude}",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }) {// TODO Make FsTextButton
                Text("Save Changes")
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
        viewState = SettingsViewState(
            apiKey = "8675309",
            longitude = 40.73f,
            latitude = -73.99f,
        )
    )
}