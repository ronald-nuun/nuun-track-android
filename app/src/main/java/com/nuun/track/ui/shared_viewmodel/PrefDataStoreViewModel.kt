package com.nuun.track.ui.shared_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.pref_data_store.PrefDataStoreManager
import com.nuun.track.domain.auth.response.UserDomain
import com.nuun.track.utility.qualifier.PrefDataStoreManagerQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefDataStoreViewModel @Inject constructor(
    @PrefDataStoreManagerQualifier private val prefDataStoreManager: PrefDataStoreManager,
) : ViewModel() {

    val userInfo = prefDataStoreManager.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun storeUser(data: UserDomain) {
        viewModelScope.launch {
            prefDataStoreManager.storeUser(data)
        }
    }

    fun clearDataStore() {
        viewModelScope.launch {
            prefDataStoreManager.clearDataStore()
        }
    }
}