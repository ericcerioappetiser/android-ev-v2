package com.appetiser.feature.report.network.di

import com.appetiser.feature.report.network.BlockRemoteSource
import com.appetiser.feature.report.network.BlockRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BlockRemoteSourceModule {

    @Binds
    @Singleton
    fun bindsBlockRemoteSource(
        blockRemoteSource: BlockRemoteSourceImpl
    ): BlockRemoteSource
}
