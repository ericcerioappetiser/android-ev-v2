package com.appetiser.feature.auth.data.di

import com.appetiser.feature.auth.data.features.session.SessionLocalSource
import com.appetiser.feature.auth.data.features.session.SessionLocalSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface SessionLocalSourceModule {

    @Binds
    @Singleton
    fun bindsSessionLocalSource(
        impl: SessionLocalSourceImpl
    ): SessionLocalSource
}
