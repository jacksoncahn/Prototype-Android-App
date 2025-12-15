package com.jetbrains.kmpapp.screens.trip

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.jetbrains.kmpapp.theme.AppThemeObject.colors
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

            // TODO: Implement this SECURELY
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
    val editingHomeBase = remember { mutableStateOf(false) }

    val homeBase = remember {mutableStateOf(Housing())}

    val coroutineScope = rememberCoroutineScope()

    val onLocationSelected: (Place) -> Unit = { place ->
        if (trip != null) {
            handleLocationSelected(place, trip, userViewModel, coroutineScope)
        }
        editingHomeBase.value = false
    }


    var detailedActivitiesList by remember { mutableStateOf<List<Activity?>>(emptyList()) }


    LaunchedEffect(trip) {
        if (trip != null) {
            detailedActivitiesList = fetchActivitiesDetails(trip)
            homeBase.value = trip.housingReadable
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
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

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                ), // Rounded corners at the top
                color = Color.Black
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = tabs.indexOf(selectedTab),
                        containerColor = Color.Black,          // background of the tab row
                        contentColor = Color.White,            // default content color
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(
                                    tabPositions[tabs.indexOf(selectedTab)]
                                ),
                                color = Color.White             // indicator color
                            )
                        }
                    ) {
                        tabs.forEach { title ->
                            Tab(
                                selected = selectedTab == title,
                                onClick = { selectedTab = title },
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.Gray,
                                text = {
                                    Text(title)
                                }
                            )
                        }
                    }
                    when (selectedTab) {
                        "My Trip" -> {
                            if (detailedActivitiesList.isNotEmpty()) {
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
                        }
                        "Trip Details" -> {
                            if (id != null) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Trip Details", color = Color.White,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (editingHomeBase.value) {
                                        Button(
                                            onClick = { editingHomeBase.value = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.White,
                                                contentColor = Color.Black
                                            ),
                                            modifier = Modifier.align(Alignment.End)) {
                                            Text("cancel search")
                                        }
                                        MapsSearchBar(onLocationSelected)
                                    } else {
                                        if (homeBase.value.address == null || homeBase.value.address == "null") {
                                            Button(
                                                onClick = { editingHomeBase.value = true },

                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.White,
                                                    contentColor = Color.Black
                                                ),
                                            )
                                            {
                                                Text("click to add your home base")
                                            }

                                        } else if (homeBase.value.address != null) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(homeBase.value.address!!, modifier = Modifier.fillMaxWidth(
                                                    0.6f
                                                ), color = Color.White)
                                                Button(
                                                    onClick = {

                                                        coroutineScope.launch {
                                                            println("CLICKED REMOVE HOME BASE")
                                                            userViewModel.createOrUpdateTrip(id = id, primaryUser = userViewModel.user!!.id, housing = Housing(null, null))
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.White,
                                                        contentColor = Color.Black
                                                    )
                                                    )
                                                {
                                                    Text("Remove housing")
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Button(
                                                onClick = {
                                                    editingHomeBase.value = true },

                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.White,
                                                    contentColor = Color.Black)
                                            )
                                                {
                                                Text("New home base")
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                            }
                        }
                        "Add Activities" -> { // Recommendations
                            if (id != null) {
                                LaunchedEffect(trip) {
                                    val location = trip?.housingReadable?.location
                                    if (location != null) {
                                        recs = RecommendByDistance(
                                            location,
                                            userViewModel.wanttogoActivities,
                                            trip
                                        )
                                    } else if (detailedActivitiesList.isNotEmpty() && trip != null) {
                                        recs = RecommendByDistance(
                                            detailedActivitiesList[0]!!.location,
                                            userViewModel.wanttogoActivities,
                                            trip
                                        )
                                    }
                                }
                            }

                            if (trip != null && trip.housingReadable.location == null) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    "Please add a home base to your trip to get activity recommendations",
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                                    color = Color.White
                                )
                            } else {
                                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                    item {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            text = "Click an activity to add it to your trip",
                                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                                            color = Color.White
                                        )
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
                                                    coroutineScope.launch {
                                                        onRecAdded(
                                                            activity,
                                                            userViewModel,
                                                            coroutineScope,
                                                            trip
                                                        )
                                                    }
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
        }
    }
}
