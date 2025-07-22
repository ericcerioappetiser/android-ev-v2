package com.appetiser.androidecv.di

import com.appetiser.core.network.SessionProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @Singleton
    abstract fun bindSessionProvider(provider: SampleSessionProvider): SessionProvider

    @Binds
    @Singleton
    abstract fun bindAuthInterceptor(provider: AuthInterceptor): Interceptor
}
