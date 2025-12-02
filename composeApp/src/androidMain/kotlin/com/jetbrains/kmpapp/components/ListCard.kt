package com.jetbrains.kmpapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mapnook.api.posts.Activity

@Composable
fun ListCard(
    activity: Activity,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showCheckbox: Boolean = false,
    onClicked: () -> Unit,
    title: String? = null,
    showDeleteIcon: Boolean = false, // Controls visibility of the delete icon
    onDeleteClicked: () -> Unit = {} // Callback for delete action
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(
                color = Color.Black.copy(alpha = .5f), shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClicked() }
            .height(120.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = activity.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp).widthIn(max = 150.dp).height(70.dp)) {
            if (title == null) {
                Text(activity.name ?: "Unnamed Location", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text(title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showCheckbox) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 16.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White
                )
            )
        }

        if (showDeleteIcon) {
            IconButton(onClick = onDeleteClicked, modifier = Modifier.padding(end = 8.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Trip", tint = Color.White)
            }
        }
    }
}
