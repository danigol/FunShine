package com.daniellegolinsky.funshine.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.funshine.R.string
import com.daniellegolinsky.themeresources.R
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsTextButton
import com.daniellegolinsky.funshinetheme.components.FsTextField
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.ui.ScreenConstants
import com.daniellegolinsky.funshine.ui.info.LocationPermissionInfoDialog
import com.daniellegolinsky.funshinetheme.components.FsAppBar
import com.daniellegolinsky.funshinetheme.components.FsForwardButton
import com.daniellegolinsky.funshinetheme.components.FsIconButton
import com.daniellegolinsky.funshinetheme.components.FsLocationButton
import com.daniellegolinsky.funshinetheme.components.FsTwoStateSwitch
import com.daniellegolinsky.funshinetheme.designelements.getBackgroundColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewState = viewModel.settingsViewState.collectAsState()
    val hasSeenLocationWarning = viewState.value.hasSeenLocationWarning
    val localContext = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(localContext)
    }
    val coarseLocationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) { granted ->
        viewModel.getApproximateLocation(locationGranted = granted, locationClient = locationClient)
    }

    // Only show the warning if they haven't seen it and we don't have permission
    if (!hasSeenLocationWarning && !coarseLocationPermissionState.status.isGranted) {
        AlertDialog(onDismissRequest = {
            dismissLocationWarning(viewModel = viewModel)
        } ) {
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

    // TODO TODOs all around!
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(top = ScreenConstants.SCREEN_PADDING)
    ) {
        FsAppBar(
            headingText = stringResource(string.settings_heading),
            backButtonAction = { navController.navigateUp() }
        )
        Spacer(modifier = Modifier.height(64.dp))
        // Content
        Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
            FsText(
                text = "Latitude, Longitude: ",
                textStyle = getBodyFontStyle(),
                modifier = Modifier.align(alignment = Alignment.Start)
            )
            FsTextField( // TODO Update with local text, then save? Or direct to viewstate?
                value = viewState.value.latLong,
                onValueChange = { viewModel.setViewStateLocation(it) },
                trailingIcon = @Composable {
                    if (viewState.value.isLoadingLocation) {
                        FsIconButton(
                            buttonIcon = painterResource(R.drawable.ic_loading_black),
                            buttonIconContentDescription = stringResource(id = string.loading)) {}
                    } else {
                        FsLocationButton(modifier = Modifier.height(16.dp)) {
                            viewModel.setViewStateLocation("0.00,0.00")
                            if (coarseLocationPermissionState.status.isGranted) {
                                viewModel.getApproximateLocation(coarseLocationPermissionState.status.isGranted, locationClient)
                            } else {
                                // TODO May eventually be able to pull this out of view state
                                //      only here still because it needs to be in the view, but also within a lambda
                                //      can't just add it to the view state like the location warning dialog.
                                if (viewState.value.hasBeenPromptedForLocationPermission) {
                                    if (viewState.value.grantedPermissionLastTime) {
                                        coarseLocationPermissionState.launchPermissionRequest()
                                    } else {
                                        // If they denied the permission last time, we have to just
                                        //      show them instructions to add it backs
                                        viewModel.setViewStateHasSeenLocationWarning(false)
                                    }
                                } else {
                                    coarseLocationPermissionState.launchPermissionRequest()
                                    viewModel.setViewStateHasBeenPromptedForLocationPermission(true)
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // ** Unit Options ** //
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(12.dp)
            ){
                item {
                    FsTwoStateSwitch(
                        optionOneString = stringResource(id = string.option_c),
                        optionTwoString = stringResource(id = string.option_f),
                        optionTwoSelected = viewState.value.isFahrenheit,
                        onOptionChanged = { viewModel.setIsFahrenheit(!viewState.value.isFahrenheit) },
                    )
                }
                item {
                    FsTwoStateSwitch(
                        optionOneString = stringResource(id = string.option_mm),
                        optionTwoString = stringResource(id = string.option_in),
                        optionTwoSelected = viewState.value.isInch,
                        onOptionChanged = { viewModel.setIsInch(!viewState.value.isInch) },
                    )
                }
                item {
                    FsTwoStateSwitch(
                        optionOneString = stringResource(id = string.option_kmh),
                        optionTwoString = stringResource(id = string.option_mph),
                        optionTwoSelected = viewState.value.isMph,
                        onOptionChanged = { viewModel.setIsMph(!viewState.value.isMph) },
                    )
                }
            }
            // ** End Unit Options ** //
            Spacer(modifier = Modifier.height(64.dp))
            FsTextButton(
                buttonText = stringResource(id = R.string.button_save_settings),
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                viewModel.saveSettings()
                // Go back to the weather screen
                // Note: We navigate here, not using back in case of changes
                //       or the user wants to go back and change a setting quickly
                navController.navigate(MainNavHost.WEATHER)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.75f))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                FsForwardButton(
                    buttonText = stringResource(id = string.settings_about)
                ) {
                    navController.navigate(MainNavHost.ABOUT)
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
        rememberNavController(),
    )
}

private fun dismissLocationWarning(viewModel: SettingsViewModel) {
    viewModel.setViewStateHasSeenLocationWarning(true)
    viewModel.saveSettings()
}
