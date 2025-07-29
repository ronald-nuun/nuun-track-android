package com.nuun.track.core.configs.networking

import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManager
import com.nuun.track.domain.auth.response.TokenDomain
import com.nuun.track.utility.qualifier.EncryptedPrefManagerQualifier
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @EncryptedPrefManagerQualifier private val pref: EncryptedPrefManager,
) {

    private val _tokenState = MutableStateFlow<TokenDomain?>(null)


    init {
        _tokenState.value = TokenDomain(
            pref.getToken(),
        )
    }

    fun logout() {
        _tokenState.value = null
        pref.clearCredentials()
    }
}