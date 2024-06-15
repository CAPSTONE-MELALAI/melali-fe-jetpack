package com.example.melali.util

import com.example.melali.mainUiViewModel


object SnackbarHandler {
    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        action: () -> Unit = {}
    ){
        mainUiViewModel.showSnackbar.value = true
        mainUiViewModel.snackbarMessage.value = message
        mainUiViewModel.snackbarActionText.value = actionLabel
        mainUiViewModel.snackbarAction.value = action
    }
}