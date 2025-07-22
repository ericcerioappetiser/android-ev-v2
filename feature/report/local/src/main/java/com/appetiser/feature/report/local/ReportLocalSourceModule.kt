package com.appetiser.feature.report.local

import com.appetiser.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportLocalSourceModule {

    @Provides
    @Singleton
    fun provideLocalSource(
        appDatabase: AppDatabase,
    ): ReportLocalSource = ReportLocalSourceImpl(
        reportDao = appDatabase.reportDao(),
        appDatabase = appDatabase
    )
}
