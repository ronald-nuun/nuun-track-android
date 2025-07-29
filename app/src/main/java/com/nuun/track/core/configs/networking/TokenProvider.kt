package com.nuun.track.core.configs.networking

import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManager
import com.nuun.track.utility.qualifier.EncryptedPrefManagerQualifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    @EncryptedPrefManagerQualifier private val pref: EncryptedPrefManager
) {
    private var authToken: String? = null

    fun generateAuthHeader() {
        val accessToken = pref.getToken()
        authToken = "Bearer $accessToken"
    }

    fun getToken(): String? = authToken
}