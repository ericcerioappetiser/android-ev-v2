package com.appetiser.androidecv.di

import com.appetiser.core.network.SessionProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppNetworkModule {

    @Provides
    @Singleton
    fun providesAuthInterceptor(authProvider: SessionProvider) = AuthInterceptor(authProvider)
}
