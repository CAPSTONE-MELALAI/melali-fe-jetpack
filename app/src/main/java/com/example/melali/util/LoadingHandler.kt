package com.example.core_ui.util

import android.annotation.SuppressLint
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.melali.mainUiViewModel

class LoadingHandler {
    @OptIn(ExperimentalMaterialApi::class)
    var pullRefreshState: PullRefreshState? = null

    fun show() {
        mainUiViewModel.showLoading.value = true
    }

    fun dismissLoading() {
        mainUiViewModel.showLoading.value = false
    }

    fun dismissRefresh() {
        mainUiViewModel.showRefresh.value = false
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun observeRefresh(
        owner: LifecycleOwner,
        onRefresh: () -> Unit
    ) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mainUiViewModel.isRefreshObserved.value = true
                }

                Lifecycle.Event.ON_PAUSE -> {
                    mainUiViewModel.isRefreshObserved.value = false
                    mainUiViewModel.showRefresh.value = false
                }

                else -> {
                    /*Let this empty*/
                }
            }
        }

        LaunchedEffect(key1 = mainUiViewModel.showRefresh.value) {
            if (mainUiViewModel.showRefresh.value) {
                onRefresh()
            }
        }

        DisposableEffect(key1 = owner) {
            owner.lifecycle.addObserver(
                observer = observer
            )

            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("ComposableNaming")
@Composable
fun rememberLoadingState(): LoadingHandler {
    val state = rememberPullRefreshState(
        refreshing = mainUiViewModel.showLoading.value || mainUiViewModel.showRefresh.value,
        onRefresh = {
            mainUiViewModel.showRefresh.value = true
        }
    )

    return rememberSaveable(
        saver = mapSaver(
            save = { mapOf() },
            restore = {
                LoadingHandler()
            }
        )
    ) {
        LoadingHandler().apply {
            this.pullRefreshState = state
        }
    }
}