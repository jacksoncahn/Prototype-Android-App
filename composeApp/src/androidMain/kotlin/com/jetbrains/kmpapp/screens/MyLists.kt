package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MyLists(navController: NavController) { // NavController is now a parameter
    var selectedListType by remember { mutableStateOf("want to go") }

    Box(modifier = Modifier.fillMaxSize()) { // Use a Box to allow overlaying the button
        IconButton(
            onClick = { navController.popBackStack() }, // Navigates back
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp) // Added padding to move the button
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp) // Increased the size of the icon
            )
        }

        Column {
            // Spacer pushes the content down to clear the menu area.
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "My Lists",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            // This Row implements the two column selector.
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedListType = "want to go" }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Want to go",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedListType = "visited" }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Visited",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedListType = "skipped" }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Skipped",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedListType = "not for me" }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Not for me",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Display content based on the selected type.
            if (selectedListType == "want to go") {
                Text("Lists of future locations")
            } else if (selectedListType == "visited") {
                Text("Lists of visited locations")
            } else if (selectedListType == "skipped") {
                Text("Lists of skipped locations")
            } else if (selectedListType == "not for me") {
                Text("Lists of locations not for you")
            }
        }
    }
}
