package com.nuun.track.domain.reservation.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateReservationRequest(
    val reservationId: RequestBody,
    val customerId: RequestBody,
    val vehicleId: RequestBody,
    val files: List<MultipartBody.Part>
)
