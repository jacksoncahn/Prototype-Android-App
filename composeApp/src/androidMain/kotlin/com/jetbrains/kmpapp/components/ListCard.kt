package com.jetbrains.kmpapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.R

@Composable
fun ListCard() {
    Row(modifier = Modifier.padding(24.dp).background(color = Color.LightGray.copy(alpha = .5f),  shape = RoundedCornerShape(8.dp)).fillMaxWidth()) {

        Row(modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)) {
            Image(painterResource(id = R.drawable.placeholder_loc), contentDescription = "Activity Image", modifier = Modifier.height(100.dp).width(100.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)

            Column(modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically)) {
                Text("Placeholder Activity English", style = MaterialTheme.typography.bodyLarge)
                Text("Placeholder Activity Czech")
            }
        }
    }
}

@Composable
@Preview
fun ListCardPreview() {
    ListCard()
}
