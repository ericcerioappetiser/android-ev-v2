package com.appetiser.core.database.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.appetiser.core.database.utils.LocalSharedPrefProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalSourceModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext application: Context): SharedPreferences = LocalSharedPrefProvider.create(application as Application, false)
}
