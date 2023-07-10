package com.daniellegolinsky.funshine.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    // Named dependencies
    const val IO_DISPATCHER = "IO_DISPATCHER"
    const val DATABASE_SCOPE = "DATABASE_SCOPE"
    const val SETTINGS_DATASTORE = "SETTINGS_DATASTORE"

    @Provides
    @Singleton
    @Named(IO_DISPATCHER)
    internal fun providesIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @Named(DATABASE_SCOPE)
    internal fun providesDatabaseScope(@Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(dispatcher)
    }

    @Provides
    @Singleton
    @Named(SETTINGS_DATASTORE)
    fun providePreferencesDataStore(@ApplicationContext appContext: Context,
                                    @Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher
    ): DataStore<Preferences> {
        val weatherSettingsDataStoreName = "weather_settings"
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(dispatcher + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(weatherSettingsDataStoreName) }
        )
    }

    @Provides
    @Singleton
    fun providesWeatherSettingsDataStore(@Named(SETTINGS_DATASTORE) dataStore: DataStore<Preferences>): WeatherSettingsDataStore {
        return WeatherSettingsDataStore(dataStore)
    }
}
