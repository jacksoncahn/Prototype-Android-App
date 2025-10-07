package com.jetbrains.kmpapp.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyLists(listType: String) {
    if (listType == "want to go")
        Text("Lists of future locations")
    else if (listType == "visited")
        Text("Lists of visited locations")
    else
    Text("Lists of locations")
}