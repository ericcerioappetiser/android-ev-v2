package com.appetiser.baseplate.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object KotlinxSerializationModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        /**
         * Temporarily added explicitNulls here because 'avatar' field is inconsistent
         * in some auth endpoints e.g. /register & /verify
         */
        explicitNulls = false
        ignoreUnknownKeys = true
    }
}
