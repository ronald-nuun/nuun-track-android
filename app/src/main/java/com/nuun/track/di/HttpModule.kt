package com.nuun.track.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.nuun.track.core.configs.networking.HeaderInterceptor
import com.nuun.track.core.configs.networking.TokenManager
import com.nuun.track.core.configs.networking.TokenProvider
import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManager
import com.nuun.track.utility.qualifier.EncryptedPrefManagerQualifier
import com.nuun.track.utility.qualifier.OkhttpQualifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideTokenProvider(
        @EncryptedPrefManagerQualifier encryptedPrefManager: EncryptedPrefManager,
    ): TokenProvider {
        return TokenProvider(
            pref = encryptedPrefManager,
        )
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @EncryptedPrefManagerQualifier encryptedPrefManager: EncryptedPrefManager,
    ): TokenManager {
        return TokenManager(
            pref = encryptedPrefManager,
        )
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        tokenProvider: TokenProvider,
    ): HeaderInterceptor {
        return HeaderInterceptor(tokenProvider = tokenProvider,)
    }

    @Provides
    @Singleton
    fun providesChuckerInterceptor(
        @ApplicationContext context: Context
    ): ChuckerInterceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .redactHeaders("**")
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    @Singleton
    @OkhttpQualifier
    fun providesOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

}