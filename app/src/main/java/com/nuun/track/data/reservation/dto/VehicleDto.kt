package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.VehicleDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDto(
    @param:Json(name = "id")
    val id: Int,
    @param:Json(name = "brand")
    val brand: String?,
    @param:Json(name = "type")
    val type: String?,
    @param:Json(name = "license")
    val license: String?,
) {
    fun toDomain(): VehicleDomain = VehicleDomain(
        id = id,
        brand = brand.orEmpty(),
        type = type.orEmpty(),
        license = license.orEmpty(),
    )
}