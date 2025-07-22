package com.appetiser.feature.report.data.di

import com.appetiser.feature.report.data.ReportRepository
import com.appetiser.feature.report.data.ReportRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReportRepositoryModule {
    @Binds
    @Singleton
    fun providesReportRepository(reportRepositoryImpl: ReportRepositoryImpl): ReportRepository
}
