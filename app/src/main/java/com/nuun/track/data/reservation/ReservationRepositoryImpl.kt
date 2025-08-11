package com.nuun.track.data.reservation

import com.nuun.track.core.configs.networking.ApiHandler
import com.nuun.track.core.configs.networking.TokenProvider
import com.nuun.track.domain.reservation.ReservationRepository
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.request.UpdateReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.squareup.moshi.Moshi

class ReservationRepositoryImpl(
    val moshi: Moshi,
    private val reservationApi: ReservationApi,
    private val token: TokenProvider
) : ReservationRepository {

    override suspend fun postReservationListQuery(request: ReservationRequest): List<ReservationDomain>? =
        ApiHandler.handleApi {
            token.generateAuthHeader()
            reservationApi.postReservation(request)
        }?.data?.map { it.toDomain() }

    override suspend fun postUpdateReservation(request: UpdateReservationRequest): ReservationDomain? = ApiHandler.handleApi {
        token.generateAuthHeader()
        reservationApi.postUpdateReservation(
            reservationId = request.reservationId,
            customerId = request.customerId,
            vehicleId = request.vehicleId,
            files = request.files
        )
    }?.data?.toDomain()

    override suspend fun postReservationDetail(request: ReservationDetailRequest): ReservationDomain? =
        ApiHandler.handleApi {
            token.generateAuthHeader()
            reservationApi.postReservationDetail(request)
        }?.data?.toDomain()
}