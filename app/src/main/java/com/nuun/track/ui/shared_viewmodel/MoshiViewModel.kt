package com.nuun.track.ui.shared_viewmodel

import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoshiViewModel @Inject constructor(
    private val moshi: Moshi
) : ViewModel() {

    fun toJson(data: Any): String {
        return moshi.adapter(Any::class.java).toJson(data)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return moshi.adapter(clazz).fromJson(json)
    }

}