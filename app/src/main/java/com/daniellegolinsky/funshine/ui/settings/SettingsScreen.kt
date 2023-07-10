package com.daniellegolinsky.funshine.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.designsystem.components.FsIconButton
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.components.FsText
import com.daniellegolinsky.designsystem.components.FsTextButton
import com.daniellegolinsky.designsystem.components.FsTextField
import com.daniellegolinsky.designsystem.font.getBodyFontStyle
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var viewState = viewModel.settingsViewState.collectAsState()
    // TODO TODOs all around!
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Faux heading line/appbar (I always found them a bit ugly, but a back arrow is okay?)
        FsIconButton(
            buttonIcon = R.drawable.ic_arrow_left_black,
            buttonIconContentDescription = R.string.ic_settings_button_back,
            onClick = { navController.navigateUp() }
        )
        // TODO Do we want to just make an app bar with the title? Or is it obvious with just 2 screens?
        Spacer(modifier = Modifier.height(64.dp))
        // Content
        Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
            FsText(
                text = "API Key: ",
                textStyle = getBodyFontStyle(),
                modifier = Modifier.align(alignment = Alignment.Start)
            )
            FsTextField(
                value = viewState.value.apiKey,
                onValueChange = { viewModel.updateViewStateApiKey(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            FsText(
                text = "Latitude, Longitude: ",
                textStyle = getBodyFontStyle(),
                modifier = Modifier.align(alignment = Alignment.Start)
            )
            FsTextField( // TODO Update with local text, then save? Or direct to viewstate?
                value = "${viewState.value.latitude}, ${viewState.value.longitude}",
                onValueChange = { },
                trailingIcon = @Composable {
                    FsIconButton(
                        buttonIcon = R.drawable.ic_button_precise_location,
                        buttonIconContentDescription = R.string.ic_precise_location_button,
                        modifier = Modifier.height(16.dp)
                    ) {
                        // TODO Get location from GPS (requires permissions)
                        // TODO Disable/don't show if they haven't granted permissions?
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(64.dp))
            FsTextButton(
                buttonText = R.string.button_save_settings,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                viewModel.saveSettings()
                // Go back to the weather screen
                // Note: We navigate here, not using back in case of changes
                //       or the user wants to go back and change a setting quickly
                navController.navigate(MainNavHost.WEATHER)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
//        viewState = SettingsViewState(
//            apiKey = "8675309",
//            latitude = 40.73f,
//            longitude = -73.99f,
//        ),
        viewModel(),
        rememberNavController()
    )
}
