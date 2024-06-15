package com.example.melali.presentation.list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ListScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<ListViewModel>()
}