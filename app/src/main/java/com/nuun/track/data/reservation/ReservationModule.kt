package com.nuun.track.data.reservation

import com.nuun.track.core.configs.networking.TokenProvider
import com.nuun.track.data.auth.AuthApi
import com.nuun.track.domain.reservation.ReservationRepository
import com.nuun.track.utility.consts.AppConsts
import com.nuun.track.utility.qualifier.OkhttpQualifier
import com.nuun.track.utility.qualifier.ReservationApiQualifier
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
object ReservationModule {

    @Provides
    @Singleton
    @ReservationApiQualifier
    fun providesReservationApi(
        @OkhttpQualifier okHttpClient: OkHttpClient
    ): ReservationApi {
        return Retrofit.Builder()
            .baseUrl(AppConsts.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ReservationApi::class.java)
    }

    @Provides
    @Singleton
    fun providesReservationRepository(
        @ReservationApiQualifier reservationApi: ReservationApi,
        moshi: Moshi,
        tokenProvider: TokenProvider
    ): ReservationRepository = ReservationRepositoryImpl(
        moshi = moshi,
        reservationApi = reservationApi,
        token = tokenProvider
    )
}