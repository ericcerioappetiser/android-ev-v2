package com.appetiser.core.network.di

import android.content.Context
import android.content.Intent
import com.appetiser.core.network.AuthApiService
import com.appetiser.core.network.BuildConfig
import com.appetiser.core.network.R
import com.appetiser.core.network.utils.Constant.API
import com.appetiser.core.network.utils.Constant.ENDPOINT_FORMAT
import com.appetiser.core.network.utils.Constant.VERSION
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun providesIoExceptionInterceptor(context: Context): Interceptor = Interceptor { chain ->
        try {
            chain.proceed(chain.request())
        } catch (exception: IOException) {
            context.applicationContext.sendBroadcast(
                Intent(
                    context.getString(R.string.intent_action_io_exception)
                ).apply {
                    setPackage(context.packageName)
                }
            )

            throw exception
        }
    }

    @Provides
    @Singleton
    fun providesChuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor
            .Builder(context)
            .collector(ChuckerCollector(context, false))
            .maxContentLength(200_000L)
            .alwaysReadResponseBody(true)
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        chuckerInterceptor: ChuckerInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            logging.redactHeader("Cookie")
            builder
                .addInterceptor(logging)
                .addInterceptor(chuckerInterceptor)
        }

        builder
            .addInterceptor(providesIoExceptionInterceptor(context))
            .retryOnConnectionFailure(true)
            .connectTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)

        builder.addInterceptor(authInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(
            String.format(
                ENDPOINT_FORMAT,
                BuildConfig.BASE_URL,
                API,
                VERSION
            )
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)
}

