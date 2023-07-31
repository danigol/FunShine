package com.daniellegolinsky.funshine.ui.settings

import android.annotation.SuppressLint
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.themeresources.R
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsTextButton
import com.daniellegolinsky.funshinetheme.components.FsTextField
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.ui.info.LocationPermissionInfoDialog
import com.daniellegolinsky.funshinetheme.components.FsBackButton
import com.daniellegolinsky.funshinetheme.components.FsLocationButton
import com.daniellegolinsky.funshinetheme.designelements.getBackgroundColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.math.RoundingMode

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    showDialog: Boolean = true,
    modifier: Modifier = Modifier
) {
    var viewState = viewModel.settingsViewState.collectAsState()
    val showFirstLaunchDialog = remember { mutableStateOf(showDialog) }
    val locationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(localContext)
    }

    if (locationPermissionState.status.isGranted && showFirstLaunchDialog.value) {
        AlertDialog(onDismissRequest = { showFirstLaunchDialog.value = false } ) {
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
                        buttonText = "Sounds good!"
                    ) {
                        showFirstLaunchDialog.value = false
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
            .padding(top = 16.dp)
    ) {
        // Faux heading line/appbar (I always found them a bit ugly, but a back arrow is okay?)
        FsBackButton() {
            navController.navigateUp()
        }
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
                value = viewState.value.latLong,
                onValueChange = { viewModel.updateViewStateLocation(it) },
                trailingIcon = @Composable {
                    FsLocationButton(modifier = Modifier.height(16.dp)) {
                        viewModel.updateViewStateLocation("0.00,0.00") // TODO Make a real loading state
                        if (locationPermissionState.status.isGranted) {
                            // TODO Definitely don't like this here
                            scope.launch(Dispatchers.IO) {
                                val result = locationClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    CancellationTokenSource().token,
                                ).addOnCompleteListener {// TODO yeah, don't like this here
                                    val locationResult = it.result
                                    val latitude = locationResult.latitude.toBigDecimal().setScale(3, RoundingMode.UP).toFloat()
                                    val longitude = locationResult.longitude.toBigDecimal().setScale(3, RoundingMode.UP).toFloat()
                                    viewModel.updateViewStateLocation("${latitude},${longitude}")
                                }
                            }
                        } else {
                            locationPermissionState.launchPermissionRequest()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
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
