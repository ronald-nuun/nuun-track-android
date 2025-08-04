package com.nuun.track.data.reservation

import com.nuun.track.core.configs.networking.ApiHandler
import com.nuun.track.core.configs.networking.TokenProvider
import com.nuun.track.domain.auth.request.LoginRequest
import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.domain.reservation.ReservationRepository
import com.squareup.moshi.Moshi

class ReservationRepositoryImpl(
    val moshi: Moshi,
    private val reservationApi: ReservationApi,
    private val token: TokenProvider
) : ReservationRepository {

    private val loginAdapter = moshi.adapter(LoginRequest::class.java)

    override suspend fun postReservationListQuery(request: ReservationRequest): List<ReservationDomain>? =
        ApiHandler.handleApi {
            token.generateAuthHeader()
            reservationApi.postReservation(request)
        }?.data?.map { it.toDomain() }

}