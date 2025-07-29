package com.nuun.track.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.pref_data_store.PrefDataStoreManager
import com.nuun.track.navigation.events.SplashNavEvent
import com.nuun.track.utility.qualifier.PrefDataStoreManagerQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    @PrefDataStoreManagerQualifier private val prefDataStoreManager: PrefDataStoreManager,
) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<SplashNavEvent?>(null)
    val navigationEvent: StateFlow<SplashNavEvent?> = _navigationEvent.asStateFlow()

    fun decideNavigation() {
        viewModelScope.launch {
            delay(2000) // Delay for 2s
            val user = prefDataStoreManager.getUser().firstOrNull()
            _navigationEvent.emit(
                if (user != null) {
                    SplashNavEvent.ToHomepage
                } else {
                    SplashNavEvent.ToLogin
                }
            )
        }
    }
}