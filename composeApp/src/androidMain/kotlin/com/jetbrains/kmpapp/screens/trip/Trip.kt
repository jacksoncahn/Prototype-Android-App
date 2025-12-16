package com.jetbrains.kmpapp.screens.trip

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.jetbrains.kmpapp.components.ActivityCard
import com.jetbrains.kmpapp.components.ActivityCardLarge
import com.jetbrains.kmpapp.components.ListCard
import com.jetbrains.kmpapp.components.MapsSearchBar
import com.jetbrains.kmpapp.utils.shortlisting.RecommendByDistance
import com.mapnook.api.activities.ActivitiesViewModel
import com.mapnook.api.user.UserViewModel
import com.mapnook.api.activities.Activity
import com.mapnook.api.trips.Housing
import com.mapnook.api.trips.Trip
import com.mapnook.api.activities.fetchActivity
import kotlinx.coroutines.launch

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

private suspend fun fetchActivitiesDetails(trip: Trip) : MutableList<Activity> {
    val activities = mutableListOf<Activity>()
    for (activity in trip.tripActivitiesReadable) {
        if (activity.activityId == null) continue
        val newActivity = fetchActivity(activity.activityId!!)
        if (newActivity != null) {
            activities += newActivity
        }
    }

    return activities

}

//private fun onRecAdded(activity: Activity, userViewModel: UserViewModel, coroutineScope: kotlinx.coroutines.CoroutineScope, trip: Trip) {
//    val activityId = activity.id
//    // Create a new list containing all existing activities plus the new one
//    val newTripActivity = TripActivity(activityId = activityId, day = null)
//    val updatedTripActivities = trip.tripActivitiesReadable.toMutableList() + newTripActivity
//    coroutineScope.launch {
//        userViewModel.createOrUpdateTrip(trip.id, trip.primaryUser, tripActivities = updatedTripActivities)
//    }
//}

@SuppressLint("UnrememberedMutableState")
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

    val scope = rememberCoroutineScope()

    val userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val activityViewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    //any vars or vals that are created using "by remember" will TRIGGER RECOMPOSITIONS when they change
    //so when we change housing, e.g. change editingHomeBase (or some other such variable with the type detailed above), we recompose everything,  create a new trip val, etc.
    //this is probably inefficient, but it works atm

    //also, we are fetching full (detailed versions of ->) activities using activityId stored in trip dataclass, WHEN THIS COMPOSABLE COMPOSES
    //because to do so when we initially fetch trips would cause the trip list population to take a noticeably long time, especially if the user navigates straight to that page

    var selectedTab by remember { mutableStateOf("My Trip") }

    val tabs = listOf("My Trip", "Recommended", "Trip Details")

    val trip = userViewModel.trips.find { it.id.toString() == id }

    var recs by remember { mutableStateOf(emptyList<Activity>()) }

    var selectedActivity by remember {mutableStateOf(null as Activity?)}

    // Add ability to set this to false while also showing mapssearchbar
    val editingHomeBase = remember { mutableStateOf(false) }

    var homeBase = remember {mutableStateOf(Housing())}

    val coroutineScope = rememberCoroutineScope()

    val onLocationSelected: (Place) -> Unit = { place ->
        if (trip != null) {
            handleLocationSelected(place, trip, userViewModel, coroutineScope)
        }
        editingHomeBase.value = false
    }


    LaunchedEffect(Unit) {
        if (trip != null) {
            val activities = fetchActivitiesDetails(trip)
            if (activities != emptyList<Activity>()) {
               userViewModel.tempTripActivities = activities
            }
        }
    }

    LaunchedEffect(trip) {
        if (trip != null) {
            homeBase.value = trip.housingReadable

        }

    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (selectedActivity != null) {
            Box (modifier = Modifier.align(Alignment.Center)) {
                IconButton(
                    onClick = {selectedActivity = null},
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .zIndex(1f) // ensures it hovers above

                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
                ActivityCardLarge(Modifier, mutableStateOf(true), selectedActivity!!, coroutineScope, true)
            }
            if (selectedTab == "My Trip") {
                IconButton(
                    onClick = {
                        if (trip != null) {
                            scope.launch{
                                userViewModel.updateTripActivities(trip.id!!,
                                    trip.primaryUser!!,
                                    selectedActivity!!,
                                    true
                                ); selectedActivity = null
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
                        .zIndex(1f) // ensures it hovers above4

                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete from trip",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            } else if (selectedTab == "Recommended") {
                IconButton(
                    onClick = {
                        if (trip != null) {
                            scope.launch{
                                userViewModel.updateTripActivities(trip.id!!,
                                    trip.primaryUser!!,

                                    selectedActivity!!

                                );  selectedActivity = null}
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
                        .zIndex(1f) // ensures it hovers above

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to trip",
                        tint = Color.White,
                        modifier = Modifier.size(67.dp)
                    )
                }
            }
        } else {
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

                        if (selectedTab == "My Trip") {
                            if (userViewModel.tempTripActivities != emptyList<Activity>()) {
                                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                                    items(userViewModel.tempTripActivities) { activity ->
                                        ListCard(
                                            activity = activity,
                                            isSelected = false, // Not selectable on this screen
                                            onCheckedChange = {}, // No action
                                            showCheckbox = false, // Hide the checkbox,
                                            onClicked = {selectedActivity = activity},
                                            showDeleteIcon = true,
                                            onDeleteClicked = {
                                                if (trip != null) {
                                                    scope.launch{userViewModel.updateTripActivities(trip.id!!,
                                                        trip.primaryUser!!, activity, true)}
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        } else if (selectedTab == "Trip Details") {
                            if (id != null) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Trip Details",
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (editingHomeBase.value) {
                                        MapsSearchBar(onLocationSelected)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { editingHomeBase.value = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.White,
                                                contentColor = Color.Black
                                            ),
                                            modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                            Text("cancel")
                                        }
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
                                                Spacer(modifier = Modifier.width(16.dp))
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
                                                    Text("Remove")
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

                        } else { // Recommendations
                            // Recommendations Tab UI
                            if (id != null) {
                                LaunchedEffect(trip) {
                                    val location = trip?.housingReadable?.location
                                    if (location != null) {
                                        recs = RecommendByDistance(
                                            location,
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
                                            text = "Add activities to your trip",
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
                                                    selectedActivity = activity
                                                }
                                            },
                                            tripActivity = true,
                                            onAddClicked = {
                                                if (trip != null) {
                                                    scope.launch{userViewModel.updateTripActivities(trip.id!!, trip.primaryUser!!, activity)}
                                                }
                                            }
                                        )
                                    }
//                            if (trip != null && trip.housingReadable.location == null) {
//                                Spacer(modifier = Modifier.height(20.dp))
//                                Text(
//                                    "Please add a home base to your trip to get activity recommendations",
//                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
//                                    color = Color.White
//                                )
//                            } else {
//                                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
//                                    item {
//                                        Spacer(modifier = Modifier.height(20.dp))
//                                        Text(
//                                            text = "Click an activity to add it to your trip",
//                                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
//                                            color = Color.White
//                                        )
//                                        Spacer(modifier = Modifier.height(8.dp))
//                                    }
//                                    items(recs) { activity ->
//                                        ListCard(
//                                            activity = activity,
//                                            isSelected = false, // Not selectable on this screen
//                                            onCheckedChange = {}, // No action
//                                            showCheckbox = false, // Hide the checkbox
//                                            onClicked = {
//                                                if (trip != null) {
//                                                    coroutineScope.launch {
//                                                        userViewModel.updateTripActivities(trip.id!!, userViewModel.user!!.id!!, activity)
//                                                    }
//                                                }
//                                            }
//                                        )
//                                    }
//                                }
                                }

                            }

                        }
                    }
                }
            }
        }
    }

}
