package com.appetiser.core.user.network.di

import com.appetiser.core.user.network.feature.UserRemoteSource
import com.appetiser.core.user.network.feature.UserRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface UserNetworkModule {

    @Binds
    @Singleton
    fun bindUserRemoteSource(impl: UserRemoteSourceImpl): UserRemoteSource
}
