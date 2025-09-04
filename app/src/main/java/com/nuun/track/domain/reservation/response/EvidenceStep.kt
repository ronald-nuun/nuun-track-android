package com.nuun.track.domain.reservation.response

import android.net.Uri
import androidx.annotation.DrawableRes

data class EvidenceStep(
    val title: String,
    val isCompleted: Boolean = false,
    val imageUri: Uri? = null,
    @DrawableRes val exampleImage: Int,
)
