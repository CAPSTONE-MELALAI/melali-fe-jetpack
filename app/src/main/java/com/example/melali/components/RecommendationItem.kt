package com.example.melali.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.melali.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationItem(
    url: String,
    name: String,
    location: String,
    onClick:() -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    ElevatedCard(
        modifier = Modifier
            .height(280.dp)
            .width(screenWidth.dp * 5 / 10),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = R.drawable.dummy_destination_image,
                contentDescription = ""
            )
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color.Transparent,Color.Black))).alpha(0.3f))
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(text = name, color = Color.White ,style = TextStyle(fontSize = 16.sp))
                Text(text = location,color = Color.White, style = TextStyle(fontSize = 12.sp))
            }
        }

    }
}