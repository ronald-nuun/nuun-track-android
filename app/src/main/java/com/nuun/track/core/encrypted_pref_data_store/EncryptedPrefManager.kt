package com.nuun.track.core.encrypted_pref_data_store

import com.nuun.track.domain.auth.response.TokenDomain
import kotlinx.coroutines.flow.Flow

interface EncryptedPrefManager {

    fun saveCredential(at: String)

    fun saveToken(tokenDomain: TokenDomain)

    fun getToken(): String?

    fun getRefreshToken(): String?

    fun setAllowFingerPrint(status: Boolean)

    fun getAllowFingerPrint(): Flow<Boolean>

    fun setAllowPatternLock(status: Boolean)

    fun getAllowPatternLock(): Flow<Boolean>

    fun clearCredentials()

}