package com.nuun.track.data.reservation

import com.nuun.track.core.configs.base.dto.BaseResponseWrapper
import com.nuun.track.data.reservation.dto.ReservationDto
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.request.ReservationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReservationApi {

    @POST("api/reservations/search")
    suspend fun postReservation(
        @Body request: ReservationRequest
    ): Response<BaseResponseWrapper<List<ReservationDto>>>

    @Multipart
    @POST("api/reservations/update")
    suspend fun postUpdateReservation(
        @Part("reservation_id") reservationId: RequestBody,
        @Part("customer_id") customerId: RequestBody,
        @Part("vehicle_id") vehicleId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<BaseResponseWrapper<ReservationDto>>

    @POST("api/reservations/details")
    suspend fun postReservationDetail(
        @Body request: ReservationDetailRequest
    ): Response<BaseResponseWrapper<ReservationDto>>

}