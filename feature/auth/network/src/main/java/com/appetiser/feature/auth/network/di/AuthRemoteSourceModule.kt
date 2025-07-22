package com.appetiser.feature.auth.network.di

import com.appetiser.feature.auth.network.AuthRemoteSource
import com.appetiser.feature.auth.network.AuthRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthRemoteSourceModule {

    @Binds
    @Singleton
    fun bindsAuthRemoteSource(impl: AuthRemoteSourceImpl): AuthRemoteSource
}
