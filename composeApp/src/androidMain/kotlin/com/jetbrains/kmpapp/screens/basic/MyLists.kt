package com.jetbrains.kmpapp.screens.basic

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.posts.MyPostsViewModel
import com.mapnook.api.posts.Post

@Composable
fun MyLists(navigateTo: (String) -> Unit, listType: String?) {

    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    var selectedListType by remember { mutableStateOf("") }

    LaunchedEffect(listType) { // React to changes in listType
        selectedListType = listType ?: "wanttogo"
    }

    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        IconButton(
            onClick = {navigateTo("home")},
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
            Spacer(modifier = Modifier.height(40.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="wanttogo"}.then(if (selectedListType == "wanttogo") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Want to go",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="visited"}.then(if (selectedListType == "visited") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Visited",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="skipped"}.then(if (selectedListType == "skipped") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Skipped",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="notforme"}.then(if (selectedListType == "notforme") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Not for me",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
            }

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
                            onCheckedChange = {checked ->
                                post.id?.let { id ->
                                    selectedIds = if (selectedIds.contains(id)) {
                                        selectedIds - id
                                    } else {
                                        selectedIds + id
                                    }
                                }
                            },
                            onClicked = {
                                navigateTo("home")
                                //viewModel.selectedPost = post
                            },
                            showDeleteIcon = true,
                            showCheckbox = false,

                            onDeleteClicked = {
                                post.id?.let { id ->
                                    when (selectedListType) {
                                        "wanttogo" -> viewModel.removeFromWantToGo(id)
                                        "visited" -> viewModel.removeFromVisited(id)
                                        "skipped" -> viewModel.removeFromSkipped(id)
                                        "notforme" -> viewModel.removeFromNotForMe(id)

                                    }
                                }
                            },
//                            isSelected = selectedIds.contains(post.id),
//                            onCheckedChange = {
//                                post.id?.let { id ->
//                                    selectedIds = if (selectedIds.contains(id)) {
//                                        selectedIds - id
//                                    } else {
//                                        selectedIds + id
//                                    }
//                                }
//                            },
                            //showCheckbox = selectedListType == "wanttogo",

                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("This list is empty.", color = Color.White)
                }
            }
        }

//        if (selectedListType == "wanttogo") {
//            val isEnabled = selectedIds.isNotEmpty()
//            Button(
//                onClick = {
//                    val ids = selectedIds.joinToString(",")
//                    navigateTo("tripplanner?ids=${Uri.encode(ids)}")
//                },
//                enabled = isEnabled,
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White.copy(alpha = if (isEnabled) 1f else 0.5f),
//                    contentColor = Color.Black,
//                    disabledContainerColor = Color.White.copy(alpha = 0.5f),
//                    disabledContentColor = Color.Black.copy(alpha = 0.5f)
//                )
//            ) {
//                //val text = if (isEnabled) "Create Trip from ${selectedIds.size} places" else "Select places to create a trip"
//                //Text(text)
//            }
//        }
    }
}
