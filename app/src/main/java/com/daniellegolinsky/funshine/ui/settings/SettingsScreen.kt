package com.daniellegolinsky.funshine.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daniellegolinsky.funshine.R.string
import com.daniellegolinsky.themeresources.R
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsTextButton
import com.daniellegolinsky.funshinetheme.components.FsTextField
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshine.ui.ScreenConstants
import com.daniellegolinsky.funshine.ui.info.LocationPermissionInfoDialog
import com.daniellegolinsky.funshine.ui.weather.LoadingScreen
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshinetheme.components.FsAppBar
import com.daniellegolinsky.funshinetheme.components.FsForwardButton
import com.daniellegolinsky.funshinetheme.components.FsIconButton
import com.daniellegolinsky.funshinetheme.components.FsLocationButton
import com.daniellegolinsky.funshinetheme.components.FsTwoStateSwitch
import com.daniellegolinsky.funshinetheme.designelements.getBackgroundColor
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyleWithoutShadow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    returnToWeatherScreen: () -> Unit,
    cancelAndGoBack: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {

    when (val viewState = viewModel.settingsViewState.collectAsStateWithLifecycle().value) {
        is ViewState.Loading -> {
            LoadingScreen()
        }

        is ViewState.Error -> {
            // TODO Shouldn't happen, currently never set to error
        }

        is ViewState.Success -> {
            val viewData = viewState.data
            val hasSeenLocationWarning = viewData.hasSeenLocationWarning
            val localContext = LocalContext.current
            val locationClient = remember {
                LocationServices.getFusedLocationProviderClient(localContext)
            }
            val coarseLocationPermissionState = rememberPermissionState(
                permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) { granted ->
                viewModel.getApproximateLocation(
                    locationGranted = granted,
                    locationClient = locationClient
                )
            }

            // Only show the warning if they haven't seen it and we don't have permission
            if (!hasSeenLocationWarning && !coarseLocationPermissionState.status.isGranted) {
                AlertDialog(onDismissRequest = {
                    dismissLocationWarning(viewModel = viewModel)
                }) {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(getBackgroundColor())
                                .padding(horizontal = 8.dp, vertical = 16.dp)
                        ) {
                            LocationPermissionInfoDialog()
                            FsTextButton(
                                buttonText = stringResource(id = string.sounds_good)
                            ) {
                                dismissLocationWarning(viewModel = viewModel)
                            }
                        }
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = ScreenConstants.SCREEN_PADDING)
            ) {
                FsAppBar(
                    headingText = stringResource(string.settings_heading),
                    backButtonAction = { cancelAndGoBack() }
                )
                Spacer(modifier = Modifier.height(64.dp))
                // Content
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp)
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    FsText(
                        text = "Latitude, Longitude: ", // TODO NOOOOOOO How did I miss this?
                        textStyle = getBodyFontStyle(),
                        modifier = Modifier.align(alignment = Alignment.Start)
                    )
                    FsTextField(
                        value = viewData.latLong,
                        onValueChange = { viewModel.setViewStateLocation(it) },
                        trailingIcon = @Composable {
                            if (viewData.isLoadingLocation) {
                                FsIconButton(
                                    buttonIcon = painterResource(R.drawable.ic_loading_black),
                                    buttonIconContentDescription = stringResource(id = string.loading)
                                ) {}
                            } else {
                                FsLocationButton(modifier = Modifier.height(16.dp)) {
                                    viewModel.setViewStateLocation("0.00,0.00")
                                    if (coarseLocationPermissionState.status.isGranted) {
                                        viewModel.getApproximateLocation(
                                            coarseLocationPermissionState.status.isGranted,
                                            locationClient
                                        )
                                    } else {
                                        // TODO May eventually be able to pull this out of view state
                                        //      only here still because it needs to be in the view, but also within a lambda
                                        //      can't just add it to the view state like the location warning dialog.
                                        if (viewData.hasBeenPromptedForLocationPermission) {
                                            if (viewData.grantedPermissionLastTime) {
                                                coarseLocationPermissionState.launchPermissionRequest()
                                            } else {
                                                // If they denied the permission last time, we have to just
                                                //      show them instructions to add it backs
                                                viewModel.setHasSeenLocationWarning(false)
                                            }
                                        } else {
                                            viewModel.setHasBeenPromptedForLocationPermission(true)
                                            coarseLocationPermissionState.launchPermissionRequest()
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    // ** Unit Options ** //
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FsTwoStateSwitch(
                            optionOneString = stringResource(id = string.option_c),
                            optionTwoString = stringResource(id = string.option_f),
                            optionTwoSelected = viewData.isFahrenheit,
                            onOptionChanged = { viewModel.setIsFahrenheit(!viewData.isFahrenheit) },
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(0.25f))
                        FsTwoStateSwitch(
                            optionOneString = stringResource(id = string.option_mm),
                            optionTwoString = stringResource(id = string.option_in),
                            optionTwoSelected = viewData.isInch,
                            onOptionChanged = { viewModel.setIsInch(!viewData.isInch) },
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FsTwoStateSwitch(
                            optionOneString = stringResource(id = string.option_kmh),
                            optionTwoString = stringResource(id = string.option_mph),
                            optionTwoSelected = viewData.isMph,
                            onOptionChanged = { viewModel.setIsMph(!viewData.isMph) },
                        )
                    }
                    // ** End Unit Options ** //

                    // Button location options
                    // TODO: Can we detect a flip phone and have an "Only on outside screen" option?
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FsText(
                            text = stringResource(string.button_side_option_label),
                            textStyle = getBodyFontStyleWithoutShadow()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FsTwoStateSwitch(
                            optionOneString = "Left",
                            optionTwoString = "Right",
                            optionTwoSelected = viewData.weatherButtonsOnRight,
                            onOptionChanged = {
                                viewModel.setWeatherButtonsOnRight(!viewData.weatherButtonsOnRight)
                            },
                        )
                    }

                    // Save and about buttons
                    Spacer(modifier = Modifier.height(64.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FsTextButton(
                            buttonText = stringResource(id = R.string.button_save_settings)
                        ) {
                            viewModel.saveSettings()
                            returnToWeatherScreen()
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight(0.75f) // TODO This doesn't really work with a scroll area
                            .defaultMinSize(minHeight = 96.dp)
                    )
                }
                Row(
                    horizontalArrangement = if (viewData.weatherButtonsOnRight) Arrangement.End else Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = ScreenConstants.DOUBLE_SCREEN_PADDING,
                            end = ScreenConstants.DOUBLE_SCREEN_PADDING,
                            bottom = ScreenConstants.SCREEN_PADDING,
                        )

                ) {
                    FsForwardButton(
                        buttonText = stringResource(id = string.settings_about)
                    ) {
                        navigateToAbout()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
        hiltViewModel(),
        {},{}, {},
    )
}

private fun dismissLocationWarning(viewModel: SettingsViewModel) {
    viewModel.setHasSeenLocationWarning(true)
    viewModel.saveSettings()
}
