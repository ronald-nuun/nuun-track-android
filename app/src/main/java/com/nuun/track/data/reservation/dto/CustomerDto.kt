package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.CustomerDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CustomerDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "phone")
    val phone: String? = null
) {
    fun toDomain(): CustomerDomain = CustomerDomain(
        id = id,
        name = name.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty()
    )
}
