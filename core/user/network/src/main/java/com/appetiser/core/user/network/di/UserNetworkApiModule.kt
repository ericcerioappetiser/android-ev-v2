package com.appetiser.core.user.network.di

import com.appetiser.core.user.network.OnboardingApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserNetworkApiModule {

    @Provides
    @Singleton
    fun providesOnboardingApiServices(retrofit: Retrofit): OnboardingApiService =
        retrofit.create(OnboardingApiService::class.java)
}
