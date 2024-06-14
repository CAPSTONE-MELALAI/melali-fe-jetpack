package com.example.core_ui.util

import com.example.core_ui.mainviewmodel.mainUiViewModel

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