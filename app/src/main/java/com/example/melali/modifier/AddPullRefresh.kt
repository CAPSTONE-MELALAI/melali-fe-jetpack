package com.example.melali.modifier

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.core_ui.util.LoadingHandler
import com.example.melali.mainUiViewModel

@OptIn(ExperimentalMaterialApi::class)
fun Modifier.addPullRefresh(
    /**
     * Isi dengan [rememberLoadingState()]
     */
    state: LoadingHandler,
    scrollState: ScrollState? = null
) = Modifier.apply {
    state.pullRefreshState?.let { stateNotNull ->
        pullRefresh(
            stateNotNull,
            mainUiViewModel.isRefreshObserved.value
        )
    }

    scrollState?.let { scrollNotNull ->
        verticalScroll(scrollNotNull)
    }
}