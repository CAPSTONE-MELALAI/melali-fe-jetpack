package com.example.melali.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.melali.components.RecommendationItem

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    Column(
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
                onClick = { /*TODO*/ },
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

        Column (
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Rekomendasi untuk Anda"
            )

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
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Buat Jadwal Baru")
        }

        Spacer(modifier = Modifier)
    }
}