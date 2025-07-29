package com.nuun.track.ui.screens.homepage

import androidx.lifecycle.ViewModel
import com.nuun.track.core.configs.state.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(

) : ViewModel() {

    private val _pageViewState by lazy {
        MutableStateFlow<ViewState<*>>(ViewState.Loading)
    }

    val pageViewState: StateFlow<ViewState<*>> get() = _pageViewState.asStateFlow()

    fun setPageViewState(viewState: ViewState<*>) {
        _pageViewState.value = viewState
    }

    private val _isRefreshing by lazy { MutableStateFlow(false) }
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    fun updateIsRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    private val _query by lazy { MutableStateFlow("") }
    val query: StateFlow<String> get() = _query.asStateFlow()

    fun setQuery(query: String) {
        _query.value = query
    }

}