package com.daniellegolinsky.funshine.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hey, welcome to the weather! ☀️"
    }
    val text: LiveData<String> = _text
}