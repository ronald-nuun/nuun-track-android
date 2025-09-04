package com.nuun.track.navigation.screens

import android.net.Uri
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.utility.consts.ExtraConst
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

sealed class HomeNavScreen(val route: String) {

    companion object {
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    object Homepage : HomeNavScreen("Homepage")
    object ReservationDetail : HomeNavScreen(
        "ReservationDetail?${ExtraConst.EXTRA_RESERVATION}={${ExtraConst.EXTRA_RESERVATION}}&${ExtraConst.EXTRA_REFRESH}={${ExtraConst.EXTRA_REFRESH}}"    ) {
        fun createRoute(item: ReservationDomain, refresh: Boolean = false): String {
            val json = moshi.adapter(ReservationDomain::class.java).toJson(item)
            val encoded = Uri.encode(json)
            return "ReservationDetail?${ExtraConst.EXTRA_RESERVATION}=$encoded&${ExtraConst.EXTRA_REFRESH}=$refresh"
        }
    }
    object ReservationForm: HomeNavScreen("ReservationForm/{${ExtraConst.EXTRA_RESERVATION}}") {
        fun createRoute(item: ReservationDomain): String {
            val json = moshi.adapter(ReservationDomain::class.java).toJson(item)
            return "ReservationForm/${Uri.encode(json)}"

        }
    }
}