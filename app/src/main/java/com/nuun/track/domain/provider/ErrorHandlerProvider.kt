package com.nuun.track.domain.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner

interface ErrorHandlerProvider {
    fun generalError(context: Context, lifecycle: LifecycleOwner, throwable: Throwable)
}