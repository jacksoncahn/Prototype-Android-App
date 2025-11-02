package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.Post

@Composable
fun MyLists(navController: NavController, viewModel: MyPostsViewModel, listType: String) { // Accept the ViewModel
    var selectedListType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        println("listType, $listType")
        selectedListType = listType
    }

    // Use a set of String for the IDs
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) { // Use a Box to allow overlaying buttons
        IconButton(
            onClick = { navController.navigate("home") },
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

        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "My Lists",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                // Your 4 tabs Column code remains the same
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="wanttogo"}.then(if (selectedListType == "wanttogo") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Want to go",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="visited"}.then(if (selectedListType == "visited") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Visited",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="skipped"}.then(if (selectedListType == "skipped") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Skipped",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="notforme"}.then(if (selectedListType == "notforme") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Not for me",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
            }

            // Get the correct list based on the selected tab
            val listToShow: List<Post> = when (selectedListType) {
                "wanttogo" -> viewModel.wanttogo
                "visited" -> viewModel.visited
                "notforme" -> viewModel.notforme
                "skipped" -> viewModel.skipped
                else -> emptyList()
            }

            if (listToShow.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(listToShow, key = { it.id!! }) { post ->
                        ListCard(
                            post = post,
                            isSelected = selectedIds.contains(post.id),
                            onCheckedChange = {
                                post.id?.let { id ->
                                    selectedIds = if (selectedIds.contains(id)) {
                                        selectedIds - id
                                    } else {
                                        selectedIds + id
                                    }
                                }
                            },
                            showCheckbox = selectedListType == "wanttogo",
                            onClicked = {navController.navigate("home"); viewModel.selectedPost = post}
                        )
                    }
                }
            } else {
                // Placeholder for other lists
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("This list is empty.", color = Color.White)
                }
            }
        }

        if (selectedListType == "wanttogo") {
            val isEnabled = selectedIds.isNotEmpty()
            Button(
                onClick = {
                    val ids = selectedIds.joinToString(",")
                    navController.navigate("tripplanner?ids=$ids")
                },
                enabled = isEnabled,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = if (isEnabled) 1f else 0.5f),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f),
                    disabledContentColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                val text = if (isEnabled) "Create Trip from ${selectedIds.size} places" else "Select places to create a trip"
                Text(text)
            }
        }
    }
}
