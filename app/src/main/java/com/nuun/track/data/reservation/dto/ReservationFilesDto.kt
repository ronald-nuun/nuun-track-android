package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.ReservationFilesDomain
import com.nuun.track.utility.enums.FileTypes
import com.nuun.track.utility.extensions.orZero
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReservationFilesDto(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "file_url")
    val fileUrl: String?,
    @Json(name = "file_type")
    val fileType: FileTypes?,
    @Json(name = "created_at")
    val createdAt: String?
) {
    fun toDomain(): ReservationFilesDomain = ReservationFilesDomain(
        id = id.orZero(),
        url = url.orEmpty(),
        fileUrl = fileUrl.orEmpty(),
        fileType = fileType,
        createdAt = createdAt.orEmpty()
    )
}
