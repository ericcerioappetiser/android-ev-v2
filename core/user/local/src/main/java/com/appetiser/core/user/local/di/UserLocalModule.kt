package com.appetiser.core.user.local.di

import com.appetiser.core.database.AppDatabase
import com.appetiser.core.database.utils.DatabaseHelper
import com.appetiser.core.database.utils.UserTypeConverters
import com.appetiser.core.user.local.feature.UserLocalSource
import com.appetiser.core.user.local.feature.UserLocalSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserLocalModule {

    @Provides
    @Singleton
    fun providesOnboardingDatabase(
        databaseHelper: DatabaseHelper, userTypeConverters: UserTypeConverters
    ): AppDatabase = AppDatabase.createInstance(databaseHelper, userTypeConverters)

    @Provides
    @Singleton
    fun providesOnboardingLocalSource(userDatabase: AppDatabase): UserLocalSource = UserLocalSourceImpl(userDatabase)
}
