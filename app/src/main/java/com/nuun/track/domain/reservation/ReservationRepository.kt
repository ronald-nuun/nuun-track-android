package com.nuun.track.domain.reservation

import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain

interface ReservationRepository {

    suspend fun postReservationListQuery(request: ReservationRequest): List<ReservationDomain>?

}