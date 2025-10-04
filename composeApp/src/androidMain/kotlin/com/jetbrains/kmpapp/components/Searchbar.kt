package com.jetbrains.kmpapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Searchbar() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        // 1. Replaced `label` with `placeholder` to prevent the text from moving up.
        placeholder = {
            Text("Find your adventure", color = Color.Gray)
        },
        trailingIcon = {
            IconButton(onClick = { search() }) {
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black,
            disabledContainerColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            // 2. Made both focused and unfocused text colors consistent.
            focusedTextColor = Color.LightGray,
            unfocusedTextColor = Color.LightGray,
            cursorColor = Color.LightGray
        )
    )
}

fun search() {
    // Placeholder for search functionality
}

@Preview
@Composable
fun SearchbarPreview() {
    //Scaffold is not available in commonMain, so we use a Box for preview
        Searchbar()
}
