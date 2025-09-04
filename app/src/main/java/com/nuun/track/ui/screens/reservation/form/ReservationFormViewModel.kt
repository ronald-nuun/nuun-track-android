package com.nuun.track.ui.screens.reservation.form

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.request.UpdateReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.domain.reservation.usecase.UpdateReservationUseCase
import com.nuun.track.utility.extensions.uriToMultipart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ReservationFormViewModel @Inject constructor(
    private val updateReservationUseCase: UpdateReservationUseCase,
): ViewModel() {

    private val _isLoading by lazy { MutableStateFlow(false) }
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()


    private val _reservation = MutableStateFlow<ReservationDomain?>(null)
    val reservation: StateFlow<ReservationDomain?> = _reservation.asStateFlow()

    fun setReservation(data: ReservationDomain?) {
        _reservation.value = data
    }

    private val _resultUpdateReservation by lazy {
        MutableStateFlow<ResultState<ReservationDomain?>>(
            ResultState.Success(null)
        )
    }

    val resultUpdateReservation: StateFlow<ResultState<ReservationDomain?>> get() = _resultUpdateReservation.asStateFlow()

    fun updateReservation(
        context: Context,
        reservationId: Int,
        customerId: Int,
        vehicleId: Int,
        files: List<Uri>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val evidences = files.mapNotNull { uri ->
                context.uriToMultipart(uri)
            }

            val request = UpdateReservationRequest(
                reservationId = reservationId.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                customerId = customerId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                vehicleId = vehicleId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                files = evidences
            )

            updateReservationUseCase.execute(UpdateReservationUseCase.RequestValues(request)).result.let {
                _isLoading.value = false
                _resultUpdateReservation.emit(it)
            }
        }
    }

    private val _selectedUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedUris: StateFlow<List<Uri>> = _selectedUris.asStateFlow()

    fun addUri(uri: Uri) {
        if (uri !in _selectedUris.value) {
            _selectedUris.value = _selectedUris.value + uri
        }
    }

    fun clearUri() {
        _selectedUris.value = emptyList()
    }
}