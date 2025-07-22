package com.appetiser.feature.report.network.di

import com.appetiser.feature.report.network.ReportRemoteSource
import com.appetiser.feature.report.network.ReportRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReportRemoteSourceModule {

    @Binds
    @Singleton
    fun bindsReportRemoteSource(
        reportRemoteSource: ReportRemoteSourceImpl
    ): ReportRemoteSource
}
