package com.appetiser.feature.auth.data.di

import com.appetiser.feature.auth.data.AuthRepository
import com.appetiser.feature.auth.data.AuthRepositoryImpl
import com.appetiser.feature.auth.data.SessionRepository
import com.appetiser.feature.auth.data.SessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindsSessionRepository(impl: SessionRepositoryImpl): SessionRepository
}
