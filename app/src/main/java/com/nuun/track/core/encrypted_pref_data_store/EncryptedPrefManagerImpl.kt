package com.nuun.track.core.encrypted_pref_data_store

import android.content.SharedPreferences
import com.nuun.track.domain.auth.response.TokenDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class EncryptedPrefManagerImpl @Inject constructor(
    private val preferences: SharedPreferences,
) : EncryptedPrefManager {

    override fun saveCredential(at: String) {
        preferences.edit()
            .putString(AT_CREDENTIAL, at)
//            .putString(RT_CREDENTIAL, rt)
            .apply()
    }

    override fun saveToken(tokenDomain: TokenDomain) {
        preferences.edit()
            .putString(AT_CREDENTIAL, tokenDomain.accessToken)
//            .putString(RT_CREDENTIAL, tokenDomain.refreshToken)
            .apply()
    }

    override fun getToken(): String? = preferences.getString(AT_CREDENTIAL, null)

    override fun getRefreshToken(): String? = preferences.getString(RT_CREDENTIAL, null)

    private val _allowFingerPrint = MutableStateFlow(preferences.getBoolean(FINGERPRINT, false))
    private val allowFingerPrint: StateFlow<Boolean> = _allowFingerPrint.asStateFlow()

    private val _allowPatternLock = MutableStateFlow(preferences.getBoolean(PATTERN_LOCK, false))
    private val allowPatternLock: StateFlow<Boolean> = _allowPatternLock.asStateFlow()

    override fun setAllowFingerPrint(status: Boolean) {
        preferences.edit().putBoolean(FINGERPRINT, status).apply()
        _allowFingerPrint.value = status
    }

    override fun getAllowFingerPrint(): Flow<Boolean> = allowFingerPrint

    override fun setAllowPatternLock(status: Boolean) {
        preferences.edit().putBoolean(PATTERN_LOCK, status).apply()
        _allowPatternLock.value = status
    }

    override fun getAllowPatternLock(): Flow<Boolean> = allowPatternLock

    override fun clearCredentials() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val AT_CREDENTIAL = "AT_CREDENTIAL"
        private const val RT_CREDENTIAL = "RT_CREDENTIAL"
        private const val FINGERPRINT = "FINGERPRINT"
        private const val PATTERN_LOCK = "PATTERN_LOCK"
    }
}