package com.nuun.track.ui.shared_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManager
import com.nuun.track.utility.qualifier.EncryptedPrefManagerQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncryptedPrefViewModel @Inject constructor(
    @EncryptedPrefManagerQualifier private val encryptedPrefManager: EncryptedPrefManager,
) : ViewModel() {

    fun saveUserCredentials(token: String) {
        encryptedPrefManager.saveCredential(token)
    }

    fun getRefreshToken(): String? = encryptedPrefManager.getRefreshToken()

    val fingerprintIdStatus = encryptedPrefManager.getAllowFingerPrint()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun setAllowFingerprintId(data: Boolean) {
        viewModelScope.launch {
            encryptedPrefManager.setAllowFingerPrint(data)
        }
    }

    val patternLockStatus = encryptedPrefManager.getAllowPatternLock()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun setAllowPatternLock(data: Boolean) {
        viewModelScope.launch {
            encryptedPrefManager.setAllowPatternLock(data)
        }
    }

    fun clearCredentials() {
        encryptedPrefManager.clearCredentials()
    }

}