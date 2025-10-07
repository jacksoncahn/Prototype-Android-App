package com.jetbrains.kmpapp.screens

import androidx.activity.result.launch
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.jetbrains.kmpapp.components.Searchbar
import kotlinx.coroutines.launch

@Composable
fun Home() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Set the initial camera position to Prague.
        val prague = LatLng(50.0755, 14.4378)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(prague, 12f)
        }

        val scope = rememberCoroutineScope()

        // The GoogleMap composable fills the entire screen and is the bottom layer.
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        )

        // UI elements are placed on top of the map
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd) // Align this whole Row to the top-right.
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 24.dp),
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
                        color = MaterialTheme.colorScheme.surfaceVariant,
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
                    Icon(Icons.Default.Add, contentDescription = "Settings", modifier = Modifier.size(24.dp))
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                        }
                    },
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Build", modifier = Modifier.size(24.dp))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Build", modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(Icons.Default.Radar, contentDescription = "Place", modifier = Modifier.size(24.dp))
            }
        }
    }
}
