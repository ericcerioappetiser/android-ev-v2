package com.appetiser.feature.song.network.di

import com.appetiser.feature.song.network.SongApiServices
import com.appetiser.feature.song.network.remote.SongRemoteSource
import com.appetiser.feature.song.network.remote.SongRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SongNetworkModule {

    @Provides
    @Singleton
    fun providesSongApiServices(retrofit: Retrofit): SongApiServices = retrofit.create(SongApiServices::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SongNetworkRemoteModule {

    @Binds
    @Singleton
    abstract fun bindSongRemoteSource(provider: SongRemoteSourceImpl): SongRemoteSource
}
