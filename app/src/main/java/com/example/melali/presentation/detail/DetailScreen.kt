package com.example.melali.presentation.detail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DetailScreen(
    navController: NavController,
    index: Long
) {
    val viewModel = hiltViewModel<DetailViewModel>()
}