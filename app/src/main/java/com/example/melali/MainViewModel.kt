package com.example.melali

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

lateinit var mainUiViewModel: MainViewModel

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {
    val showLoading = mutableStateOf(false)
    val showRefresh = mutableStateOf(false)
    val isRefreshObserved = mutableStateOf(false)

    val showSnackbar = mutableStateOf(false)
    val snackbarMessage = mutableStateOf("")
    val snackbarActionText = mutableStateOf<String?>(null)
    val snackbarAction = mutableStateOf({})

    val showBottombar = mutableStateOf(false)
    val currentRoute = mutableStateOf("splash")

    val backClicked = mutableStateOf(false)

    fun resetSnackbarData(){
        snackbarMessage.value = ""
        snackbarActionText.value = null
        snackbarAction.value = {}
        showSnackbar.value = false
    }
}