package com.nuun.track.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.core.configs.state.ViewState
import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.domain.reservation.usecase.ReservationListQueryUseCase
import com.nuun.track.utility.logger.ApiLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val reservationListQueryUseCase: ReservationListQueryUseCase,
) : ViewModel() {

    val apiLogger = ApiLogger()

    private val _pageViewState by lazy {
        MutableStateFlow<ViewState<List<ReservationDomain>>>(ViewState.Loading)
    }

    val pageViewState: StateFlow<ViewState<List<ReservationDomain>>> get() = _pageViewState.asStateFlow()

    fun setPageViewState(viewState: ViewState<List<ReservationDomain>>) {
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

    private val _resultReservation by lazy { MutableStateFlow<ResultState<List<ReservationDomain>?>>(
        ResultState.Success(emptyList())) }
    val resultReservation: StateFlow<ResultState<List<ReservationDomain>?>> get() = _resultReservation.asStateFlow()

    fun getRecentReservation() {
        viewModelScope.launch {
            val request = ReservationRequest()
            reservationListQueryUseCase.execute(ReservationListQueryUseCase.RequestValues(request)).result.let {
                _resultReservation.emit(it)
                it.onSuccess { data ->
                    when {
                        data.isNullOrEmpty() -> setPageViewState(ViewState.Empty)
                        else -> setPageViewState(ViewState.Success(data))
                    }
                }.onError { error ->
                    setPageViewState(ViewState.Error(error))
                }
            }
        }
    }

    fun getFilteredReservation(query: String) {
        viewModelScope.launch {
            val request = ReservationRequest(query = query)
            reservationListQueryUseCase.execute(ReservationListQueryUseCase.RequestValues(request)).result.let {
                _resultReservation.emit(it)
                it.onSuccess { data ->
                    when {
                        data.isNullOrEmpty() -> setPageViewState(ViewState.Empty)
                        else -> setPageViewState(ViewState.Success(data))
                    }
                }.onError { error ->
                    setPageViewState(ViewState.Error(error))
                }
            }
        }
    }

    private val _reservationList by lazy { MutableStateFlow(emptyList<ReservationDomain>()) }
    val reservationList: StateFlow<List<ReservationDomain>> get() = _reservationList.asStateFlow()

    fun setReservationList(list: List<ReservationDomain>?) {
        _reservationList.value = list ?: emptyList()
    }
}