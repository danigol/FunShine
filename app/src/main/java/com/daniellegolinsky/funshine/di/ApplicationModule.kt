package com.daniellegolinsky.funshine.di

import android.content.Context
import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providesWeatherSettingsDataStore(@ApplicationContext appContext: Context): WeatherSettingsDataStore {
        return WeatherSettingsDataStore(appContext)
    }
}
