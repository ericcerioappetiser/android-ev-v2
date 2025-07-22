package com.appetiser.feature.report.data.di

import com.appetiser.feature.report.data.BlockRepositoryImpl
import com.appetiser.feature.report.data.BlockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BlockRepositoryModule {
    @Binds
    @Singleton
    fun providesBlockRepository(blockRepository: BlockRepositoryImpl): BlockRepository
}
