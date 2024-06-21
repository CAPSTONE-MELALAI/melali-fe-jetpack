package com.example.melali.presentation.scheduling

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core_ui.util.LoadingHandler
import com.example.core_ui.util.rememberLoadingState
import com.example.melali.R
import com.example.melali.model.request.SchedulingRequest
import com.example.melali.util.SnackbarHandler
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import okhttp3.internal.toLongOrDefault
import java.util.Locale

@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalLayoutApi::class, ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun SchedulingScreen(navController: NavController) {
    val viewModel = hiltViewModel<SchedulingViewModel>()
    val locationPermission =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    val context = LocalContext.current
    val fused = LocationServices.getFusedLocationProviderClient(context)
    val initialPosition = LatLng(-8.409518, 115.188919)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
    }
    val markerState = rememberMarkerState(
        position = initialPosition
    )
    val loadingState = rememberLoadingState()

    LaunchedEffect(key1 = true) {
        if (!locationPermission.allPermissionsGranted) {
            locationPermission.launchMultiplePermissionRequest()
        } else {
            fused
                .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener {
                    val currentLoc = LatLng(it.latitude, it.longitude)
                    markerState.position = currentLoc
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLoc, 10f)
                }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getAllDestinations()
    }

    LaunchedEffect(
        key1 = viewModel.latLngSelected.value,
        key2 = viewModel.latLngSelected.value?.latitude,
        key3 = viewModel.latLngSelected.value?.longitude
    ) {
        viewModel.latLngSelected.value?.let { latLng ->
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            viewModel.locationSelectedName.value = addresses?.firstOrNull()?.getAddressLine(0)
                ?: "Lokasi sudah terpilih, namun tidak dikenali di server"
        }
    }

    if (viewModel.showMapPopup.value) {
        Dialog(
            onDismissRequest = { viewModel.showMapPopup.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        cameraPositionState = cameraPositionState,
                        onMapLongClick = {
                            markerState.position = it
                        }
                    ) {
                        Marker(
                            state = markerState,
                            draggable = true
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 24.dp),
                        onClick = {
                            viewModel.latLngSelected.value = markerState.position
                            viewModel.showMapPopup.value = false
                        }
                    ) {
                        Text(text = "Konfirmasi")
                    }
                }
            }
        }
    }

    if (viewModel.recommendationResult.value == null) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Buat Jadwal") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = ""
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            if (viewModel.selectedDestination.size < 2) {
                                SnackbarHandler.showSnackbar("Pastikan destinasi paling tidak 2 atau lebih")
                                return@Button
                            }

                            if (!viewModel.allFilled.value) {
                                SnackbarHandler.showSnackbar("Pastikan semua data telah diisi")
                                return@Button
                            }

                            loadingState.show()

                            viewModel.getRecommendation(
                                body = SchedulingRequest(
                                    idx_selected = viewModel.selectedDestination.map { it.index },
                                    budget = viewModel.budgetInput.value.toLongOrDefault(0L),
                                    days = viewModel.hariInput.value.toLongOrDefault(1L),
                                    lat_user = viewModel.latLngSelected.value?.latitude ?: .0,
                                    long_user = viewModel.latLngSelected.value?.longitude ?: .0,
                                    is_accessibility = viewModel.isDisability.value
                                ),
                                onSuccess = {
                                    it.data?.let {
                                        viewModel.recommendationResult.value = it
                                        Log.e("HASIL", it.toString())
                                    }
                                    loadingState.dismissLoading()
                                },
                                onFailed = {
                                    SnackbarHandler.showSnackbar(it.message.toString())
                                    loadingState.dismissLoading()
                                }
                            )
                        }
                    ) {
                        Text(text = "Buat Jadwal")
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.padding(top = 8.dp)) }

                item {
                    Column {
                        Text(text = "Pilih Destinasi")
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    viewModel.destinations.filter {
                                        viewModel.destinationInput.value.isEmpty() || it.place.contains(
                                            viewModel.destinationInput.value
                                        )
                                    }
                                ) { item ->
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .clickable {
                                                if (viewModel.selectedDestination.any { it.index == item.index }) {
                                                    viewModel.selectedDestination.removeIf { it.index == item.index }
                                                } else {
                                                    viewModel.selectedDestination.add(item)
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = viewModel.selectedDestination.any { it.index == item.index },
                                            onCheckedChange = {
                                                if (!it) {
                                                    viewModel.selectedDestination.removeIf { it.index == item.index }
                                                } else {
                                                    viewModel.selectedDestination.add(item)
                                                }
                                            }
                                        )
                                        Text(
                                            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                            text = item.place,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            viewModel.selectedDestination.forEach { item ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 10.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f), text = item.place
                                        )

                                        Icon(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clickable {
                                                    viewModel.selectedDestination.removeIf { it.index == item.index }
                                                },
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (!it.isFocused && viewModel.budgetInput.value.isEmpty()) {
                                    viewModel.budgetInput.value = "0"
                                }
                            },
                        value = viewModel.budgetInput.value,
                        onValueChange = {
                            if (it.isEmpty() || it.isDigitsOnly()) {
                                viewModel.budgetInput.value = it
                                return@OutlinedTextField
                            }
                        },
                        leadingIcon = {
                            Text(text = "Rp")
                        },
                        label = {
                            Text(text = "Budget")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (!it.isFocused && viewModel.hariInput.value.isEmpty()) {
                                    viewModel.hariInput.value = "1"
                                }
                            },
                        value = viewModel.hariInput.value,
                        onValueChange = {
                            if (it.isEmpty() || it.isDigitsOnly()) {
                                viewModel.hariInput.value = it
                                return@OutlinedTextField
                            }
                        },
                        trailingIcon = {
                            Text(text = "Hari")
                        },
                        label = {
                            Text(text = "Estimasi Hari")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                item {
                    Column {
                        Text(text = "Lokasi Mulai")
                        if (!locationPermission.allPermissionsGranted) {
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(horizontal = 24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            text = "Ijin Lokasi Dibutuhkan",
                                            style = MaterialTheme.typography.headlineMedium
                                        )

                                        Button(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            onClick = {
                                                context.startActivity(Intent().apply {
                                                    action =
                                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                                    data =
                                                        Uri.fromParts(
                                                            "package",
                                                            context.packageName,
                                                            null
                                                        )
                                                })
                                            }
                                        ) {
                                            Text(text = "Ijinkan dari Setting")
                                        }
                                    }
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    text = if (viewModel.locationSelectedName.value.isEmpty()) {
                                        "Pilih lokasi terlebih dahulu"
                                    } else {
                                        viewModel.locationSelectedName.value
                                    },
                                    color = if (viewModel.locationSelectedName.value.isEmpty()) {
                                        Color.Red
                                    } else {
                                        Color.Black
                                    },
                                    style = MaterialTheme.typography.labelMedium
                                )

                                TextButton(
                                    onClick = { viewModel.showMapPopup.value = true }
                                ) {
                                    Text(text = "Pilih Lokasi")
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = viewModel.isDisability.value == 1,
                            onCheckedChange = {
                                viewModel.isDisability.value = when {
                                    it -> 1
                                    else -> 0
                                }
                            }
                        )

                        Text(text = "Ramah Disabilitas")
                    }
                }

                item { Spacer(modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = R.drawable.dummy_destination_image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                }
            }

            itemsIndexed(viewModel.recommendationResult.value ?: listOf()) { index, list ->
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hari ke-${index + 1}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    list.forEachIndexed { indexDestination, item ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${indexDestination + 1}",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Column {
                                Text(text = item.place)
                            }
                        }
                    }
                }
            }
        }
    }
}