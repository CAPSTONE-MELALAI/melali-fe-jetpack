package com.example.melali.presentation.list

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.response.SingleDestinationCCResponse
import com.example.melali.util.SnackbarHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){
    val destinations = mutableStateListOf<SingleDestinationCCResponse>()
    val selectedTab = mutableStateOf(0)
    val selectedMarker = mutableStateOf<SingleDestinationCCResponse?>(null)
    fun getAllDestination() {
        viewModelScope.launch {
            repository.getAllDestinationFromCC(
                onSuccess = {
                    it.data?.let { destinations.addAll(it) }
                },
                onFailed = {
                    SnackbarHandler.showSnackbar(it.message.toString())
                    Log.e("ERR", it.toString())
                }
            )
        }
    }
}