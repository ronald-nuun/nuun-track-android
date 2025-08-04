package com.nuun.track.data.reservation

import com.nuun.track.core.configs.base.dto.BaseResponseWrapper
import com.nuun.track.data.reservation.dto.ReservationDto
import com.nuun.track.domain.reservation.request.ReservationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationApi {

    @POST("api/reservations/search")
    suspend fun postReservation(
        @Body request: ReservationRequest
    ): Response<BaseResponseWrapper<List<ReservationDto>>>

}