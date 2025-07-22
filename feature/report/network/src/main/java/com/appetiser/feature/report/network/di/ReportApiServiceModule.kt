package com.appetiser.feature.report.network.di

import com.appetiser.feature.report.network.ReportApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportApiServiceModule {
    @Provides
    @Singleton
    fun provideReportApiServices(retrofit: Retrofit): ReportApiServices =
        retrofit.create(ReportApiServices::class.java)
}
