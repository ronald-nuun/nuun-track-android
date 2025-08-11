package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.ReservationDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReservationDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "customer")
    val customer: CustomerDto? = null,
    @Json(name = "vehicle")
    val vehicle: VehicleDto? = null,
    @Json(name = "reservation_details")
    val reservationDetails: ReservationDetailDto? = null,
    @Json(name = "pricing")
    val pricing: PricingDto? = null,
    @Json(name = "timestamps")
    val timestamps: TimestampsDto? = null,
    @Json(name = "notes")
    val notes: String? = null,
    @Json(name = "files")
    val files: ReservationFilesDto? = null,
    @Json(name = "evidence_start")
    val evidenceStart: List<ReservationFilesDto>? = null,
    @Json(name = "evidence_end")
    val evidenceEnd: List<ReservationFilesDto>? = null,
) {
    fun toDomain(): ReservationDomain = ReservationDomain(
        id = id,
        customer = customer?.toDomain(),
        vehicle = vehicle?.toDomain(),
        reservationDetail = reservationDetails?.toDomain(),
        pricing = pricing?.toDomain(),
        timestamps = timestamps?.toDomain(),
        notes = notes.orEmpty(),
        files = files?.toDomain(),
        evidenceStart = evidenceStart?.map { it.toDomain() },
        evidenceEnd = evidenceEnd?.map { it.toDomain() }
    )
}