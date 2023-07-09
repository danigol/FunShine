package com.daniellegolinsky.funshine.di

import android.content.Context
import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object ApplicationModule {
    // Named dependencies
    const val IO_DISPATCHER = "IO_DISPATCHER"
    const val DATABASE_SCOPE = "DATABASE_SCOPE"

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
    fun providesWeatherSettingsDataStore(@ApplicationContext appContext: Context): WeatherSettingsDataStore {
        return WeatherSettingsDataStore(appContext)
    }
}
