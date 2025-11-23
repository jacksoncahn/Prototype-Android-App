package com.jetbrains.kmpapp.screens.trip

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.jetbrains.kmpapp.components.ListCard
import com.jetbrains.kmpapp.components.MapsSearchBar
import com.jetbrains.kmpapp.utils.shortlisting.RecommendByDistance
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.Post

@Composable
fun Trip(id: String?, onClose: () -> Unit) {

    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    var selectedTab by remember { mutableStateOf("My Trip") }

    val tabs = listOf("My Trip", "Add Activities", "Trip Details")

    val trip = viewModel.trips.find { it.id.toString() == id }
    var recs by remember { mutableStateOf(emptyList<Post>()) }

    // Add ability to set this to false while also showing mapssearchbar
    val editingHomeBase = remember {mutableStateOf(false)}

    val onLocationSelected: (Place) -> Unit = { place ->
        place.latLng?.let { trip?.baseLoc = listOf(it.latitude, it.longitude) }
        trip?.baseName = place.name
        trip?.baseAddress = place.address
        editingHomeBase.value = false
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black),) { // Wrap in a Box
        IconButton(
            onClick = onClose, // Navigates back
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp)

        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "My Trip",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(50.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), // Rounded corners at the top
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
                        tabs.forEach { title ->
                            Tab(
                                selected = selectedTab == title,
                                onClick = { selectedTab = title },
                                text = { Text(title) }
                            )
                        }
                    }

                    if (selectedTab == "My Trip") {
                        if (id != null) {
                            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                items(trip?.posts ?: emptyList()) { post ->
                                    ListCard(
                                        post = post,
                                        isSelected = false, // Not selectable on this screen
                                        onCheckedChange = {}, // No action
                                        showCheckbox = false, // Hide the checkbox,
                                        onClicked = {}
                                    )
                                }
                            }
                        }
                    } else if (selectedTab == "Trip Details") {
                        if (id != null) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Trip Details",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                var homeBase by remember { mutableStateOf("") }

                                if (editingHomeBase.value) {
                                    MapsSearchBar(onLocationSelected)
                                } else {
                                    if (trip?.baseName == null) {
                                        Button(onClick = { editingHomeBase.value = true }) {
                                            Text("click to add your home base")
                                        }
                                    } else {
                                        Text(trip.baseName.toString())
                                        Button(onClick = { editingHomeBase.value = true }) {
                                            Text("click to edit")
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                        }

                    } else { // Recommendations
                        // Recommendations Tab UI
                        if (id != null) {
                            LaunchedEffect(trip) {
                                if (trip != null) {
                                    recs = RecommendByDistance(trip.posts[0], viewModel.wanttogo, trip)
                                }
                            }

                            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                item {
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Text(text = "Click an activity to add it to your trip", modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                items(recs) { post ->
                                    ListCard(
                                        post = post,
                                        isSelected = false, // Not selectable on this screen
                                        onCheckedChange = {}, // No action
                                        showCheckbox = false, // Hide the checkbox
                                        onClicked = {trip?.posts += post; recs -= post}
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
