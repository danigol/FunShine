package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import com.daniellegolinsky.funshine.data.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(settingsRepo: SettingsRepo): ViewModel() {

}