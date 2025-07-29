package com.appetiser.feature.song.data.di

import com.appetiser.feature.song.data.repository.SongRepository
import com.appetiser.feature.song.data.repository.SongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SongRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSongRepository(songRepositoryImpl: SongRepositoryImpl): SongRepository
}
