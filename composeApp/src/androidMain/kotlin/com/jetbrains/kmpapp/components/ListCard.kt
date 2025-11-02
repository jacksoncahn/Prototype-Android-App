package com.jetbrains.kmpapp.components

import android.R.attr.height
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jetbrains.kmpapp.R
import com.mapnook.api.Post

@Composable
fun ListCard(
    post: Post,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showCheckbox: Boolean = true,
    onClicked: () -> Unit,
    title: String? = null
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(
                color = Color.LightGray.copy(alpha = .5f), shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClicked() }
            .height(120.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = post.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp).widthIn(max = 150.dp).height(70.dp)) {
            if (title == null) {
                Text(post.name ?: "Unnamed Location", style = MaterialTheme.typography.bodyLarge)

            } else {
                Text(title, style = MaterialTheme.typography.bodyLarge)
            }
//            post.nativeName?.let { Text(it) }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showCheckbox) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 16.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black
                )
            )
        }
    }
}

//@Composable
//@Preview
//fun ListCardPreview() {
//    // A sample post for the preview
//    val samplePost = Post(
//        id = "1",
//        name = "Preview Activity English",
//        nativeName = "Preview Activity Czech",
//        location = emptyList(),
//        slug = "",
//        status = "",
//        tagline = "",
//        summary = "",
//        description = "",
//        nativeLanguageCode = "",
//        primaryImageId = "",
//        tags = emptyList(),
//        osm = null,
//        properties = "",
//        nextShowTime = "",
//        importance = 0,
//        createdAt = "",
//        updatedAt = "",
//        deletedAt = ""
//    ) {
//        ListCard(post = samplePost, isSelected = true, onCheckedChange = {}, onClicked = {})
//    }
//}
