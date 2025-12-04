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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.posts.ActivitiesViewModel

@Composable
fun TripPlanner(ids: String?, onTripSaved: () -> Unit, popBackStack: () -> Unit) {

    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    // Parse the string of IDs into a list of strings
    val idList = ids?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    val allPosts = viewModel.activities + viewModel.wanttogo + viewModel.visited
    val selectedPosts = allPosts.filter { post ->
        post.id != null && idList.contains(post.id)
    }.distinctBy { it.id }

    var tripName by remember { mutableStateOf("") }
    // Combine all lists from the viewmodel that could contain the selected posts
    // Find the full Post objects that match the received IDs
    val base: LatLng

//    LaunchedEffect(selectedPosts) {
//        //adds the trip to the list of trips
//        //the Trip class is defined in MyPostsViewModel so that's where we access it from
//        viewModel.trips += MyPostsViewModel.Trip(
//            id = viewModel.trips.size + 1,
//            name = "My Trip",
//            posts = selectedPosts
//        )
//    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) { // Wrap in a Box
        IconButton(
            onClick = popBackStack,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 16.dp, end = 8.dp),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Name Your Trip",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text("Trip Name") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            // Display the list of selected posts for the preview
            LazyColumn(modifier = Modifier.padding(vertical = 8.dp).weight(1f)) {
                items(
                    selectedPosts,
                    key = { post -> (post.id?.toString()) ?: post.hashCode().toString() }) { post ->
                    ListCard(
                        activity = post,
                        isSelected = false,
                        onCheckedChange = {},
                        showCheckbox = false,
                        onClicked = {} // Does nothing for now
                    )
                }
            }

            Button(
                onClick = {
                    if (tripName.isNotBlank() && selectedPosts.isNotEmpty()) {
                        viewModel.createTrip(tripName, selectedPosts)
                        onTripSaved() // Navigate back to the trip list
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                enabled = tripName.isNotBlank() && selectedPosts.isNotEmpty()
            ) {
                Text("Save Trip")

            }
        }
    }
}
