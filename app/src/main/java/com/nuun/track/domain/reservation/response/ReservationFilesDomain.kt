package com.nuun.track.domain.reservation.response

import com.nuun.track.utility.enums.FileTypes

data class ReservationFilesDomain(
    val id: Int?,
    val fileUrl: String?,
    val fileType: FileTypes?,
    val createdAt: String?
)
