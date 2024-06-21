package com.example.melali.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.melali.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val user = viewModel.getUserFromLocal()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Box {
            AsyncImage(
                model = R.drawable.logo, contentDescription = "", modifier = Modifier.width(120.dp)
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
        Box(
            Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)) {
            AsyncImage(
                model = R.drawable.user,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row{
            Text(text = "Name", modifier = Modifier.width(86.dp))
            Text(text = user.value?.username.toString(), modifier = Modifier.weight(1f))
        }
        Row {
            Text(text = "Email", modifier = Modifier.width(86.dp))
            Text(text = user.value?.email.toString(), modifier = Modifier.weight(1f))
        }
        Row {
            Text(text = "Phone", modifier = Modifier.width(86.dp))
            Text(text = user.value?.phoneNumber.toString(), modifier = Modifier.weight(1f))
        }

        Button(
            onClick = {
                viewModel.removeUser()
                navController.navigate("home"){
                    popUpTo(navController.graph.id){
                        inclusive = true
                    }
                }
            }
        ) {
            Text(text = "Keluar")
        }
    }
}