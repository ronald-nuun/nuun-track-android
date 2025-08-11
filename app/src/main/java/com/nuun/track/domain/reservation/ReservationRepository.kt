package com.nuun.track.domain.reservation

import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.request.UpdateReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain

interface ReservationRepository {

    suspend fun postReservationListQuery(request: ReservationRequest): List<ReservationDomain>?

    suspend fun postUpdateReservation(
        request: UpdateReservationRequest
    ): ReservationDomain?

    suspend fun postReservationDetail(request: ReservationDetailRequest): ReservationDomain?

}