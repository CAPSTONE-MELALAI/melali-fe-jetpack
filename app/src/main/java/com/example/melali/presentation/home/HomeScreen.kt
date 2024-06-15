package com.example.melali.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.melali.components.RecommendationItem

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedIconButton(
                onClick = {
                    if (viewModel.user == null) {
                        viewModel.showShouldLoginPopup.value = true
                        return@OutlinedIconButton
                    }

                    //TODO NAVIGATE TO PROFIL
                },
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "")
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = viewModel.searchInput.value,
            onValueChange = {
                viewModel.searchInput.value = it
            },
            label = {
                Text(text = "Search")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rekomendasi untuk Anda"
                )

                TextButton(
                    onClick = {
                        navController.navigate("list")
                    }
                ) {
                    Text(text = "Lihat Semua")
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                items(10) {
                    RecommendationItem(url = "", name = "Pantai", location = "Ubud, bali")
                }

                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = {
                if (viewModel.user == null) {
                    viewModel.showShouldLoginPopup.value = true
                    return@Button
                }

                //TODO NAVIGATE TO BUAT JADWAL
            }
        ) {
            Text(text = "Buat Jadwal Baru")
        }

        Spacer(modifier = Modifier)
    }
}