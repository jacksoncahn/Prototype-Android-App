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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.jetbrains.kmpapp.components.ListCard
import com.jetbrains.kmpapp.components.MapsSearchBar
import com.jetbrains.kmpapp.utils.shortlisting.RecommendByDistance
import com.mapnook.api.posts.ActivitiesViewModel
import com.mapnook.auth.UserViewModel
import com.mapnook.api.posts.Activity
import com.mapnook.api.posts.Housing
import com.mapnook.api.posts.Trip
import com.mapnook.api.posts.TripActivity
import com.mapnook.api.posts.fetchActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

//helper functions
private fun handleLocationSelected(place: Place, trip: Trip, userViewModel: UserViewModel, coroutineScope: kotlinx.coroutines.CoroutineScope) {
        var latlng: List<Double> = emptyList()
        var address = ""

        place.latLng?.let { latlng = listOf(it.latitude, it.longitude) }
        address = place.address.orEmpty()

        if (latlng.isNotEmpty() && address.isNotEmpty()) {
            val housing = Housing(address, latlng)
            coroutineScope.launch {
                userViewModel.createOrUpdateTrip(trip.id, trip.primaryUser, housing = housing)
            }
        }
}

private suspend fun fetchActivitiesDetails(trip: Trip) : MutableList<Activity?> {
    val activities = mutableListOf<Activity?>()
    for (activity in trip.tripActivitiesReadable) {
        if (activity.activityId == null) continue
        activities += fetchActivity(activity.activityId!!)
    }

    return activities

}

private fun onRecAdded(activity: Activity, userViewModel: UserViewModel, coroutineScope: kotlinx.coroutines.CoroutineScope, trip: Trip) {
    val activityId = activity.id
    // Create a new list containing all existing activities plus the new one
    val newTripActivity = TripActivity(activityId = activityId, day = null)
    val updatedTripActivities = trip.tripActivitiesReadable.toMutableList() + newTripActivity
    coroutineScope.launch {
        userViewModel.createOrUpdateTrip(trip.id, trip.primaryUser, tripActivities = updatedTripActivities)
    }
}

@Composable
fun Trip(id: String?, onClose: () -> Unit) {

    //initialize places api which we use to add a home base for location recommendations
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            //currently insecure, was having trouble getting this key from local properties
            Places.initialize(context, "AIzaSyDptUiKvCPS2taP70fdpUEKcn6ib4AosI8")
        }
    }

    val userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val activityViewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )


    var selectedTab by remember { mutableStateOf("My Trip") }

    val tabs = listOf("My Trip", "Add Activities", "Trip Details")

    val trip = userViewModel.trips.find { it.id.toString() == id }

    var recs by remember { mutableStateOf(emptyList<Activity>()) }

    // Add ability to set this to false while also showing mapssearchbar
    val editingHomeBase = remember {mutableStateOf(false)}

    val coroutineScope = rememberCoroutineScope()

    val onLocationSelected: (Place) -> Unit = { place ->
        if (trip != null) {
            handleLocationSelected(place, trip, userViewModel, coroutineScope)
        }
        editingHomeBase.value = false
    }


    var detailedActivitiesList = mutableListOf<Activity?>()


    LaunchedEffect(trip != null) {
        if (trip != null) {
            detailedActivitiesList = fetchActivitiesDetails(trip)
        }
    }



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                        if (detailedActivitiesList != emptyList<Activity?>()) {
                            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                items(detailedActivitiesList) { activity ->
                                    if (activity != null) {
                                        ListCard(
                                            activity = activity,
                                            isSelected = false, // Not selectable on this screen
                                            onCheckedChange = {}, // No action
                                            showCheckbox = false, // Hide the checkbox,
                                            onClicked = {}
                                        )
                                    }
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
                                    if (trip?.housingReadable?.address == null) {
                                        Button(onClick = { editingHomeBase.value = true }) {
                                            Text("click to add your home base")
                                        }
                                    } else {
                                        Text(trip.housingReadable?.address.toString())
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
                                val location = trip?.housingReadable?.location
                                if (location != null) {
                                        recs = RecommendByDistance(location, activityViewModel.wanttogo, trip)
                                    } else if (detailedActivitiesList.isNotEmpty() && trip != null) {
                                        recs = RecommendByDistance(detailedActivitiesList[0]!!.location, activityViewModel.wanttogo, trip)
                                    }
                                }
                            }

                            if (trip != null && trip.housingReadable?.location == null) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text("Please add a home base to your trip to get smarter recommendations", modifier = Modifier.padding(start = 20.dp, end = 20.dp))}
                            }

                            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                item {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(text = "Click an activity to add it to your trip", modifier = Modifier.padding(start = 20.dp, end = 20.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                items(recs) { activity ->
                                    ListCard(
                                        activity = activity,
                                        isSelected = false, // Not selectable on this screen
                                        onCheckedChange = {}, // No action
                                        showCheckbox = false, // Hide the checkbox
                                        onClicked = {
                                            if (trip != null) {
                                                coroutineScope.launch { onRecAdded(activity, userViewModel, coroutineScope, trip) }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

