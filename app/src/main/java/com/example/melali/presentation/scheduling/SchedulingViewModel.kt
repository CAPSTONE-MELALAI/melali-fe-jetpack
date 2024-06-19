package com.example.melali.presentation.scheduling

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.request.SchedulingRequest
import com.example.melali.model.response.ResponseWrapper
import com.example.melali.model.response.SingleDestinationCCResponse
import com.example.melali.model.response.SingleDestinationMLResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulingViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val recommendationResult = mutableStateOf<List<SingleDestinationMLResponse>?>(null)
    val selectedDestination = mutableStateListOf<SingleDestinationCCResponse>()
    val destinationInput = mutableStateOf("")
    val destinations = mutableStateListOf<SingleDestinationCCResponse>()
    val budgetInput = mutableStateOf("0")
    val hariInput = mutableStateOf("1")
    val latLngSelected = mutableStateOf<LatLng?>(null)
    val locationSelectedName = mutableStateOf("")
    val isDisability = mutableStateOf(0)
    val showMapPopup = mutableStateOf(false)

    fun getAllDestinations() {
        viewModelScope.launch {
            repository.getAllDestinationFromCC(
                onSuccess = {
                    it.data?.let {
                        destinations.addAll(it)
                    }
                },
                onFailed = {
                    Log.e("ERROR", it.message.toString())
                }
            )
        }
    }

    fun getRecommendation(
        body: SchedulingRequest,
        onSuccess: (ResponseWrapper<List<SingleDestinationMLResponse>>) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.getSchedule(body, onSuccess, onFailed)
        }
    }
}