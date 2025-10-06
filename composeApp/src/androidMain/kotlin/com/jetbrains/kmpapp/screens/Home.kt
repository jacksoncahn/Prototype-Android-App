package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.components.Searchbar

@Composable
fun Home() {
    // Use a Box as the main container. It makes aligning children independently much easier.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        // 1. Align the Searchbar to the Top-End of the Box.
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd) // Align this whole Row to the top-right.
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Searchbar(modifier = Modifier.fillMaxWidth(.8f))
        }

        // 2. Align the button Column to the Center-End of the Box.
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
                    .background(color = Color.Gray, shape = RoundedCornerShape(8.dp)),
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(32.dp))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Build, contentDescription = "Build", modifier = Modifier.size(32.dp))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Build, contentDescription = "Build", modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.Gray, shape = RoundedCornerShape(8.dp))) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Place, contentDescription = "Build", modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewHome() {
//    Home()
//}