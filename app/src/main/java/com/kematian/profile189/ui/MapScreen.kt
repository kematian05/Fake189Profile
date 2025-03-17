package com.kematian.profile189.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kematian.profile189.room.ProfileRepository
import com.kematian.profile189.viewmodels.ProfileViewModel
import com.kematian.profile189.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "ContextCastToActivity"
)
@Composable
fun MapScreen(
    onProfileClicked: () -> Unit,
    viewModel: ProfileViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val azerbaijan = LatLng(40.4093, 49.8671)
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val userMarkerState = rememberMarkerState(position = currentLocation ?: azerbaijan)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(azerbaijan, 16f)
    }

    var showSearchScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLocationPermissionGranted = checkLocationPermission(context)
        if (isLocationPermissionGranted) {
            getCurrentLocation(fusedLocationClient) { location ->
                currentLocation = LatLng(location.latitude, location.longitude)
                userMarkerState.position = currentLocation ?: azerbaijan
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(currentLocation ?: azerbaijan, 16f)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        coroutineScope.launch {
            (context as? Activity)?.finish()
        }
    }

    MaterialTheme {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawer(
                        onProfileClicked = onProfileClicked,
                        onBackPress = {
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        imageUri = viewModel.profile.value?.profilePictureUri,
                        username = viewModel.profile.value?.username.toString()
                    )
                }
            },
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen
        ) {
            if (showSearchScreen) {
                SearchScreen(
                    onClose = { showSearchScreen = false },
                )
            } else {
                Scaffold {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxSize()
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(isMyLocationEnabled = isLocationPermissionGranted),
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = false,
                                myLocationButtonEnabled = false,
                                compassEnabled = false,
                            )
                        ) {
                            Marker(
                                visible = false,
                                state = userMarkerState,
                                title = "My Location",
                                snippet = "You are here"
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .systemBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
                                .fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .size(44.dp)
                                    .background(Color.White)
                                    .clickable {
                                        scope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.menu),
                                    contentDescription = "Menu",
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .size(44.dp)
                                    .background(Color.White)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {

                                Badge(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-8).dp, y = 8.dp),
                                    containerColor = Color.Red
                                ) { }

                                Icon(
                                    painter = painterResource(R.drawable.notification),
                                    contentDescription = "Notifications",
                                    tint = Color.Black
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable {
                                        cameraPositionState.position =
                                            CameraPosition.fromLatLngZoom(
                                                currentLocation ?: azerbaijan,
                                                16f
                                            )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.my_location),
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(Color(0xFFFEFEFE))
                        ) {
                            Column {
                                HorizontalDivider(
                                    thickness = 4.dp,
                                    color = Color(0xFFC7C7C7),
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .align(Alignment.CenterHorizontally)
                                        .width(80.dp)
                                )
                                Box(
                                    modifier = Modifier

                                        .padding(
                                            top = 24.dp,
                                            start = 20.dp,
                                            end = 20.dp,
                                            bottom = 18.dp
                                        )
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = 2.dp,
                                            color = Color(0xFFBCBCBC),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            showSearchScreen = true
                                        }
                                ) {
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = { /*TODO*/ },
                                        enabled = false,
                                        maxLines = 1,
                                        placeholder = {
                                            Text(
                                                text = "${viewModel.profile.value?.username}, hara gedirik?",
                                                style = TextStyle(
                                                    color = Color(0xFFBCBCBC),
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 20.sp,
                                                    lineHeight = 28.sp
                                                )
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search",
                                                tint = Color(0xFFBCBCBC),
                                                modifier = Modifier
                                                    .size(24.dp)
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent
                                        ),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun checkLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location) -> Unit
) {
    try {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onLocationReceived(it)
                }
            }
    } catch (e: SecurityException) {
        // Handle exception
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MapScreenPreview() {
    MapScreen(
        onProfileClicked = { },
        viewModel = ProfileViewModel(ProfileRepository.getInstance(LocalContext.current))
    )
}