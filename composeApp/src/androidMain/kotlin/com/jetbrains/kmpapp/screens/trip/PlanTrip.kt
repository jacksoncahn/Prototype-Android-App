package com.jetbrains.kmpapp.screens.trip

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapnook.api.posts.ActivitiesViewModel
import com.jetbrains.kmpapp.components.ListCard
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.mapnook.api.posts.Activity
import com.mapnook.api.posts.TripActivity
import com.mapnook.api.posts.fetchActivity
import com.mapnook.auth.UserViewModel

@Composable
fun PlanTrip(
    onClose: () -> Unit,
    navigateTo: (String) -> Unit
) {
    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val userViewModel: UserViewModel = viewModel(
    viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )


//    val wantToGo = remember {mutableListOf<Activity>()}

//    LaunchedEffect(userViewModel.wanttogoActivities) {
//        for (activityId in userViewModel.wanttogo) {
//          val activity = fetchActivity(activityId)
//            if (activity != null) {
//                wantToGo += activity
//            }
//        }
//    }

    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        Column(modifier = Modifier.padding(top = 60.dp)) {
            Text(
                text = "Plan a Trip",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(userViewModel.wanttogoActivities, key = { it.id!! }) { post ->
                    ListCard(
                        activity = post,
                        isSelected = selectedIds.contains(post.id),
                        showCheckbox = true,
                        onCheckedChange = {
                            post.id?.let { id ->
                                selectedIds = if (id in selectedIds) {
                                    selectedIds - id
                                } else {
                                    selectedIds + id
                                }
                            }
                        },
                        onClicked = {}
                    )
                }
            }
        }

        if (selectedIds.isNotEmpty()) {
            Button(
                onClick = {
                    val ids = selectedIds.joinToString(",")
                    navigateTo("tripplanner?ids=$ids")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Create Trip from ${selectedIds.size} places")
            }
        }
    }
}

