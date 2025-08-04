package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.VehicleDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "serial_number")
    val serialNumber: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "police_number")
    val policeNumber: String?,
) {
    fun toDomain(): VehicleDomain = VehicleDomain(
        id = id,
        serialNumber = serialNumber.orEmpty(),
        name = name.orEmpty(),
        policeNumber = policeNumber.orEmpty(),
    )
}
