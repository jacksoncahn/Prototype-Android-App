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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.posts.ActivitiesViewModel
import com.mapnook.api.posts.ActivitiesViewModel.Trip


@Composable
fun TripList(navigateTo: (String) -> Unit) {

    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    // Track which trip is being deleted
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    //Confirmation dialog
    if (tripToDelete != null) {
        AlertDialog(
            onDismissRequest = { tripToDelete = null },
            title = {Text("Delete Trip")},
            text = {Text("Are you sure you want to delete the trip \"${tripToDelete!!.name}\"?")},
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTrip(tripToDelete!!.id)
                    tripToDelete = null
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { tripToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "My Trips",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(50.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.trips, key = { it.id }) { trip ->
                    val representativePost = trip.activities.firstOrNull() ?: return@items
                    ListCard(
                        activity = representativePost,
                        isSelected = false,
                        onCheckedChange = {},
                        showCheckbox = false,
                        onClicked = { navigateTo("trip/${trip.id}") },
                        title = trip.name,
                        showDeleteIcon = true,
                        onDeleteClicked = { tripToDelete = trip }
                    )
                }
            }

            // This button now navigates to the correct list screen
            Button(
                onClick = { navigateTo("mylists/wanttogo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Create New Trip")
            }
        }

        IconButton(
            onClick = { navigateTo("home") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 26.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }
    }
}