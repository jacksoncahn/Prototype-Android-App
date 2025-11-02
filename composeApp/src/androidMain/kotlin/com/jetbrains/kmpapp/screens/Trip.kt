package com.jetbrains.kmpapp.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetbrains.kmpapp.components.ListCard
import com.mapnook.api.MyPostsViewModel

@Composable
fun Trip(id: String?, modifier: Modifier, navController: NavController) {

    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    Box(modifier = Modifier.fillMaxSize()) { // Wrap in a Box
        IconButton(
            onClick = { navController.popBackStack() }, // Navigates back
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp),
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
                text = "My Trip",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(50.dp))
            if (id != null) {
                val trip = viewModel.trips.find { it.id.toString() == id }
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    trip?.posts?.forEach { post ->
                        ListCard(
                            post = post,
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
    }


