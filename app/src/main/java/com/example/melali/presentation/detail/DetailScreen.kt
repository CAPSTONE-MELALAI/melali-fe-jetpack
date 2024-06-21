package com.example.melali.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
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

    val context = LocalContext.current

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .height(78.dp)
                            .width(78.dp)
                            .align(Alignment.Center)

                    ) {
                        AsyncImage(
                            model = R.drawable.category,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Category",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${destinasi?.category}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .height(78.dp)
                            .width(78.dp)
                            .align(Alignment.Center)

                    ) {
                        AsyncImage(
                            model = R.drawable.disability_friendly,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Disability Friendly",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (destinasi?.Is_accessibility == true) "Yes" else "No",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .height(78.dp)
                            .width(78.dp)
                            .align(Alignment.Center)

                    ) {
                        AsyncImage(
                            model = R.drawable.ticket,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ticket",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${destinasi?.price.toString().take(2)} K",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        Divider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                .padding(start = 32.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,


            ) {
            Text(
                text = "${destinasi?.address}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)

            )

            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    val mapIntentUri = Uri.parse("${destinasi?.url}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
                    context.startActivity(mapIntent)
                }, contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondaryContainer

                )
            ) {
                Text(text = "Buka Map", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Column(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${destinasi?.description}",
                maxLines = if (readMoreClicked) Int.MAX_VALUE else 5,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (!readMoreClicked) "Read More" else "Show Less",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    readMoreClicked = !readMoreClicked
                })

        }
        Spacer(modifier = Modifier.height(72.dp))

    }
}

@Preview
@Composable
fun miawmiaw() {
    Box(Modifier.background(Color.White)) {
        DetailScreen(navController = rememberNavController(), index = 1)
    }
}


