package com.nuun.track.di

import com.nuun.track.core.configs.provider.ErrorHandlerProviderImpl
import com.nuun.track.domain.provider.ErrorHandlerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerProviderModule {

    @Provides
    @Singleton
    fun providesErrorHandlerProvider(): ErrorHandlerProvider =
        ErrorHandlerProviderImpl()
}