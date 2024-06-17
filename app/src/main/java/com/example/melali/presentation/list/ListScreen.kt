package com.example.melali.presentation.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.melali.components.ListItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<ListViewModel>()
    val lazyColumnState = rememberLazyListState()
    val locationPermission =
        rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    val context = LocalContext.current
    val myLocation = remember {
        mutableStateOf(LatLng(-8.409518, 115.188919))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myLocation.value, 10f)
    }
    val fused = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    if (locationPermission.allPermissionsGranted) {
        fused.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    p0.lastLocation?.let {
                        myLocation.value = LatLng(it.latitude, it.longitude)
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.getAllDestination()
    }

    if (viewModel.selectedMarker.value != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.selectedMarker.value = null }) {
            Column(
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .padding(horizontal = 24.dp),
            ) {
                viewModel.selectedMarker.value?.let { item ->
                    Text(text = item.place, style = MaterialTheme.typography.titleLarge)
                    Text(text = item.address)
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            navController.navigate("detail/${item.index}")
                        }
                    ) {
                        Text(text = "Lihat Detail")
                    }
                }
            }
        }
    }

    Column {
        TabRow(
            selectedTabIndex = viewModel.selectedTab.value,
            containerColor = Color.White
        ) {
            Tab(
                selected = viewModel.selectedTab.value == 0,
                onClick = {
                    viewModel.selectedTab.value = 0
                },
                text = { Text(text = "List") }
            )

            Tab(
                selected = viewModel.selectedTab.value == 1,
                onClick = {
                    viewModel.selectedTab.value = 1

                    if (locationPermission.permissions.any { it.status != PermissionStatus.Granted }) {
                        locationPermission.launchMultiplePermissionRequest()
                    }
                },
                text = { Text(text = "Map") }
            )
        }

        when (viewModel.selectedTab.value) {
            0 -> {
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(viewModel.destinations) { item ->
                        ListItem(
                            url = "",
                            name = item.place,
                            location = item.address,
                            onClick = {
                                navController.navigate("detail/${item.index}")
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            1 -> {
                if (locationPermission.permissions.any { it.status != PermissionStatus.Granted }) {
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

                            Icon(
                                modifier = Modifier.size(84.dp),
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = ""
                            )

                            Text(
                                text = "Untuk menggunakan fitur ini, pastikan permission lokasi diijinkan. Anda dapat mengijinkan secara manual melalui setting.",
                                textAlign = TextAlign.Center
                            )

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    context.startActivity(Intent().apply {
                                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        data =
                                            Uri.fromParts("package", context.packageName, null)
                                    })
                                }
                            ) {
                                Text(text = "Ijinkan dari Setting")
                            }
                        }
                    }
                } else {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        viewModel.destinations.forEach { item ->
                            val loc = LatLng(item.lat, item.long)
                            Marker(
                                state = MarkerState(position = loc),
                                title = item.place,
                                snippet = item.place,
                                onClick = {
                                    viewModel.selectedMarker.value = item
                                    true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}