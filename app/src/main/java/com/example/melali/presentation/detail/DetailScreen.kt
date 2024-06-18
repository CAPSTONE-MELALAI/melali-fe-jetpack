package com.example.melali.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.melali.R
import com.example.melali.model.response.SingleDestinationCCResponse
import io.ktor.http.websocket.websocketServerAccept

@Composable
fun DetailScreen(
    navController: NavController,
    index: Long
) {

    var readMoreClicked by remember {
        mutableStateOf(false)
    }

    val viewModel = hiltViewModel<DetailViewModel>()
    var destinasi by remember {
        mutableStateOf<SingleDestinationCCResponse?>(null)
    }

    LaunchedEffect(key1 = true) {
        viewModel.getDestinationByIndex(index,
            onSuccess = {
                it.data?.let {
                    destinasi = it
                }
            },
            onFailed = {})
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )


        ) {
            Box() {
                AsyncImage(
                    model = R.drawable.dummy_destination_image,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
                    .alpha(0.3f)
            ) {

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${destinasi?.place}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = "${destinasi?.rating}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp)
                )
                AsyncImage(
                    model = R.drawable.star_rating,
                    modifier = Modifier.size(20.dp),
                    contentDescription = ""
                )
            }
        }
        Column(modifier = Modifier.padding(32.dp)) {
            Text(text = "About", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${destinasi?.description}", maxLines = if (readMoreClicked) Int.MAX_VALUE else 5, textAlign = TextAlign.Justify, overflow = TextOverflow.Ellipsis)
            Text(
                text = if (!readMoreClicked) "Read More" else "Show Less",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    readMoreClicked = !readMoreClicked
                })

        }
        Row(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            Text(text = "${destinasi?.address}", modifier = Modifier.weight(1f))
            Text(text = "Buka Map", modifier = Modifier.clickable {

            })
        }

        Row {
            Column {
                Box{
                    Box(modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .height(48.dp)
                        .width(48.dp))
                }
            }
        }

    }


}



