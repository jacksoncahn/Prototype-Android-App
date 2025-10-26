package com.jetbrains.kmpapp.screens

import androidx.compose.material.icons.filled.Remove
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.jetbrains.kmpapp.components.Searchbar
import com.jetbrains.kmpapp.components.ActivityCard
import com.jetbrains.kmpapp.theme.AppThemeObject
import com.mapnook.api.MyPostsViewModel
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Marker
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapnook.api.Post

@Composable fun Home(modifier: Modifier, isLoading: MutableState<Boolean>) {

    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val detailView = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Set the initial camera position to Prague.
        val prague = LatLng(50.0755, 14.4378)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(prague, 12f)
        }

        val locations = remember { mutableStateOf(listOf<Post>()) }

        val scope = rememberCoroutineScope()

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

            if (!isLoading.value) {
                val activities = viewModel.posts
                activities.forEach { post ->
                    val lng = post.location.getOrNull(0)
                    val lat = post.location.getOrNull(1)
                    println("loKati&n $lat, $lng")
                    if (lat != null && lng != null && post.imageUrl != null) {
//                        val bitmapState = bitmapDescriptorFromUrl(LocalContext.current, post.imageUrl)
                        Marker(
                            state = MarkerState(LatLng(lat, lng)),
//                            icon = bitmapState.value,
                            title = post.name
                        )
                    }
                }
            }

        }

            // UI elements are placed on top of the map
            Row(
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Searchbar(modifier = Modifier.fillMaxWidth(.8f))
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            color = AppThemeObject.colors.foreground,
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                            }
                        },
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Build",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.ArrowUpward,
                            contentDescription = "Build",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .background(
                            color = AppThemeObject.colors.foreground,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Radar,
                        contentDescription = "Place",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                viewModel.posts.forEach { post ->
                    ActivityCard(detailView = detailView, post = post, modifier = Modifier)
                }
            }
        }
    }


