package com.appetiser.feature.auth.data.di

import com.appetiser.feature.auth.data.features.accesstoken.AccessTokenLocalSource
import com.appetiser.feature.auth.data.features.accesstoken.AccessTokenLocalSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface AccessLocalSourceModule {

    @Binds
    @Singleton
    fun bindsAccessLocalSource(impl: AccessTokenLocalSourceImpl): AccessTokenLocalSource
}
