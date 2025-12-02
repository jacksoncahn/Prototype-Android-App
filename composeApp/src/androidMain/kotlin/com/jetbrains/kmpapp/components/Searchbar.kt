package com.jetbrains.kmpapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Searchbar(modifier: Modifier) {

    var text by remember { mutableStateOf("") }

    var isUnfocused by remember { mutableStateOf(true) }

    val placeholder = if (isUnfocused) "Find your adventure" else "Search for anything"

    //basic textfield component that has a search icon and a placeholder
    //search icon is clickable and calls the search function (currently does nothing)

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isUnfocused = !focusState.isFocused
            }
            .border(1.dp, Color.LightGray, RoundedCornerShape(50.dp)),
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        placeholder = { Text(text = placeholder, color = Color.LightGray) },
        trailingIcon = {
            IconButton(onClick = { search() }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Black,
            focusedContainerColor = Color.Black,
            cursorColor = Color.White,
            unfocusedPlaceholderColor = Color.LightGray,
            focusedPlaceholderColor = Color.LightGray,
        ),
        textStyle = MaterialTheme.typography.titleMedium.copy(color = Color.White)
    )
}

fun search() {
    // Placeholder for search functionality
}

@Preview
@Composable
fun SearchbarPreview() {
    Searchbar(modifier = Modifier)
}
