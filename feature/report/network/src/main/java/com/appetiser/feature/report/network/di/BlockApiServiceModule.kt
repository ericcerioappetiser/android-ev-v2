package com.appetiser.feature.report.network.di

import com.appetiser.feature.report.network.BlockApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BlockApiServiceModule {
    @Provides
    @Singleton
    fun provideBlockApiServices(retrofit: Retrofit): BlockApiServices =
        retrofit.create(BlockApiServices::class.java)
}
