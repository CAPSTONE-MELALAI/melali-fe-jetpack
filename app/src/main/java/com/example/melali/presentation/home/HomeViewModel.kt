package com.example.melali.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melali.data.Repository
import com.example.melali.model.response.SingleDestinationCCResponse
import com.example.melali.model.response.SingleDestinationMLResponse
import com.example.melali.util.SnackbarHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val searchInput = mutableStateOf("")
    val user = repository.getUserFromLocal()
    val showShouldLoginPopup = mutableStateOf(false)

    val destinationCC = mutableStateListOf<SingleDestinationCCResponse>()
    val destinationML = mutableStateListOf<SingleDestinationMLResponse>()

    fun getAllDestinationFromCC() {
        viewModelScope.launch {
            repository.getAllDestinationFromCC(
                onSuccess = {
                    it.data?.let {
                        destinationCC.addAll(it)
                    }
                },
                onFailed = {
                    SnackbarHandler.showSnackbar(it.message.toString())
                }
            )
        }
    }

    fun getAllDestinationFromML(userIndex: Long) {
        viewModelScope.launch {
            repository.getDestinationRecommendation(
                userIndex,
                onSuccess = {
                    it.data?.let {
                        destinationML.addAll(it)
                    }
                },
                onFailed = {
                    SnackbarHandler.showSnackbar(it.message.toString())
                }
            )
        }
    }
}