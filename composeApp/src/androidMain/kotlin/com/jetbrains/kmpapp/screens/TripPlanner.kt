package com.jetbrains.kmpapp.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.MyPostsViewModel

@Composable
fun TripPlanner(ids: String?, viewModel: MyPostsViewModel, navController: NavController) { // Add NavController
    // Parse the string of IDs into a list of strings
    val idList = ids?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

    // Combine all lists from the viewmodel that could contain the selected posts
    val allPosts = viewModel.wanttogo + viewModel.visited + viewModel.posts
    
    // Find the full Post objects that match the received IDs
    val selectedPosts = allPosts.filter { post -> idList.contains(post.id) }

    Box(modifier = Modifier.fillMaxSize()) { // Wrap in a Box
        IconButton(
            onClick = { navController.popBackStack() }, // Navigates back
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "My Trip",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            
            if (selectedPosts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Trip Planner: Start a new trip from the menu!")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(selectedPosts, key = { it.id!! }) { post ->
                        ListCard(
                            post = post,
                            isSelected = false, // Not selectable on this screen
                            onCheckedChange = {}, // No action
                            showCheckbox = false // Hide the checkbox
                        )
                    }
                }
            }
        }
    }
}
