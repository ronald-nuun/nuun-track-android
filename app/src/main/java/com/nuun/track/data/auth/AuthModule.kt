package com.nuun.track.data.auth

import com.nuun.track.domain.auth.AuthRepository
import com.nuun.track.utility.consts.AppConsts
import com.nuun.track.utility.qualifier.AuthApiQualifier
import com.nuun.track.utility.qualifier.OkhttpQualifier
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @AuthApiQualifier
    fun provideEWeLinkUserApi(
        @OkhttpQualifier okHttpClient: OkHttpClient
    ): AuthApi {
        return Retrofit.Builder()
            .baseUrl(AppConsts.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providesEWeLinkUserRepository(
        @AuthApiQualifier authApi: AuthApi,
        moshi: Moshi
    ): AuthRepository = AuthRepositoryImpl(
        moshi = moshi,
        authApi = authApi
    )

}