package com.example.melali.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.melali.components.ListItem

@Composable
fun ListScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<ListViewModel>()

    LaunchedEffect(key1 = true) {
        viewModel.getAllDestination()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(viewModel.destinations) { item ->
            ListItem(url = "", name = item.place, location = item.address)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}