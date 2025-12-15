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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mapnook.api.posts.Activity
import com.mapnook.api.posts.fetchActivity
import com.mapnook.auth.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun MyLists(navigateTo: (String) -> Unit, listType: String?) {

    val userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val activityViewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )


    val scope = rememberCoroutineScope()


//    var wanttogo by remember { mutableStateOf(emptyList<Activity>()) }
//    var notforme by remember { mutableStateOf(emptyList<Activity>()) }
//    var skipped by remember { mutableStateOf(emptyList<Activity>()) }
//    var visited by remember { mutableStateOf(emptyList<Activity>()) }

//    LaunchedEffect(userViewModel) {
//        for (activityId in userViewModel.wanttogo) {
//            val activity = fetchActivity(activityId)
////            if (activity != null) {
////                wanttogo += activity
////            }
//        }
//        for (activityId in userViewModel.notforme) {
//            val activity = fetchActivity(activityId)
////            if (activity != null) {
////                notforme += activity
////            }
//        }
//        for (activityId in userViewModel.skipped) {
//            val activity = fetchActivity(activityId)
////            if (activity != null) {
////                skipped += activity
////            }
//        }
//        for (activityId in userViewModel.visited) {
//            val activity = fetchActivity(activityId)
////            if (activity != null) {
////                visited += activity
////            }
//        }
//
//    }


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

            //titles/buttons to navigate to each different listType
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="wanttogo"}.then(if (selectedListType == "wanttogo") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Want to go",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="visited"}.then(if (selectedListType == "visited") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Visited",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="skipped"}.then(if (selectedListType == "skipped") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Skipped",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
                Column(modifier=Modifier.weight(1f).clickable{selectedListType="notforme"}.then(if (selectedListType == "notforme") Modifier.background(Color.White.copy(alpha = 0.2f)) else Modifier).padding(16.dp)){Text(text="Not for me",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,style=MaterialTheme.typography.bodySmall, color = Color.White)}
            }

            val listToShow: List<Activity> = when (selectedListType) {
                "wanttogo" -> userViewModel.wanttogoActivities
                "visited" -> userViewModel.visitedActivities
                "notforme" -> userViewModel.notformeActivities
                "skipped" -> userViewModel.skippedActivities
                else -> emptyList()
            }

            if (listToShow.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(listToShow, key = { it.id!! }) { activity ->
                        ListCard(
                            activity = activity,
                            isSelected = selectedIds.contains(activity.id),
                            onCheckedChange = {checked ->
                                activity.id?.let { id ->
                                    selectedIds = if (selectedIds.contains(id)) {
                                        selectedIds - id
                                    } else {
                                        selectedIds + id
                                    }
                                }
                            },
                            onClicked = {
                                navigateTo("home")
                                activityViewModel.selectedActivity = activity
                            },
                            showDeleteIcon = true,
                            showCheckbox = false,

                            onDeleteClicked = { scope.launch {
                                activity.id?.let { id ->
                                    when (selectedListType) {
                                        "wanttogo" -> {userViewModel.deleteUserAction(activityId = activity.id!!, type = "yes")}
                                        "visited" -> userViewModel.deleteUserAction(activityId = activity.id!!, type = "visited")
                                        "skipped" -> userViewModel.deleteUserAction(activityId = activity.id!!, type = "skipped")
                                        "notforme" -> userViewModel.deleteUserAction(activityId = activity.id!!, type = "no")
                                    }
                                }
                            }
                            },

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
    }
}
