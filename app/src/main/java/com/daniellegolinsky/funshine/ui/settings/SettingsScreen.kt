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
import androidx.compose.ui.unit.sp
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState

@Composable
fun SettingsScreen(
    viewState: SettingsViewState,
    modifier: Modifier = Modifier
) {
    // TODO TODOs all around!
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "<",
                fontSize = 32.sp,
                modifier = Modifier.padding(16.dp),
            ) // TODO Obviously, replace!
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextField(value = viewState.apiKey, onValueChange = {})
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = "${viewState.latitude}, ${viewState.longitude}", onValueChange = {})
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Save Changes")
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