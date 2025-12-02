package com.jetbrains.kmpapp.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.mapnook.api.posts.ActivitiesViewModel
import com.mapnook.api.posts.Activity

@Composable
fun ActivityCard(modifier: Modifier, detailView: MutableState<Boolean>, activity: Activity) {
    LaunchedEffect(detailView.value) {
        println("DetailView: ${detailView.value}")
    }

    if (detailView.value) {
        ActivityCardLarge(modifier = modifier.padding(bottom = 32.dp), detailView, activity)
    } else {
        ActivityCardSmall(modifier = modifier.padding(bottom = 32.dp), detailView, activity)
    }
}

@Composable
fun ActivityCardLarge(modifier: Modifier, detailView: MutableState<Boolean>, activity: Activity) {

    //should access shared viewModel for posts
    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val descriptionLarge = remember {mutableStateOf(false)}

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .padding(all = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .background(color = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                AsyncImage(
                    model = activity.imageUrl,
                    contentDescription = activity.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )


                Box(modifier = Modifier.padding(16.dp).align(TopEnd)) {
                    IconButton(onClick = { detailView.value = false }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.CallReceived, contentDescription = "Enlarge")
                    }
                }


                Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 16.dp).width(250.dp)) {
                    Text(
                        text = activity.name?: "no name available",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 8f)
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
                }

                Row(modifier = Modifier.align(BottomEnd).padding(16.dp)) {
                    IconButton(onClick = {viewModel.activityInteraction("like", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = {viewModel.activityInteraction("dislike", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                    }
                }

                Row(modifier = Modifier.align(BottomStart).padding(16.dp)) {
                    IconButton(onClick = {viewModel.activityInteraction("skip", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "skip activity")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { viewModel.activityInteraction("visited", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = "mark already visited")
                    }
                }
            }

            if (descriptionLarge.value) {
                Text(
                    color = Color.White,
                    text = (activity.description?: "no description available"),
                    modifier = Modifier.padding(16.dp).clickable { descriptionLarge.value = false }
                )
            } else {
                Text(
                    color = Color.White,
                    text = activity.summary?: "no summary available",
                    maxLines = 3,
                    modifier = Modifier.padding(16.dp).clickable { descriptionLarge.value = true }
                )
            }

            Column {
                Text(
                    "Tags",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(all = 16.dp),
                    color = Color.White
                )
                FlowRow {
                    activity.tags?.forEach { tag ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Text(
                                color = Color.White,
                                text = tag,
                                modifier = Modifier.background(color = Color.Gray, shape = RoundedCornerShape(8.dp)).padding(8.dp)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Links", color = Color.White)
                Text("fakelink@url.com", color = Color.White)
            }
        }
    }
}

@Composable
fun ActivityCardSmall(modifier: Modifier, detailView: MutableState<Boolean>, activity: Activity) {

    //should access shared viewModel for posts
    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )

    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = activity.imageUrl,
                contentDescription = activity.name,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Box(modifier = Modifier.padding(16.dp).align(TopEnd)) {
                IconButton(onClick = { detailView.value = true }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                    Icon(Icons.AutoMirrored.Filled.CallMade, contentDescription = "Enlarge")
                }
            }

            Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 16.dp).width(250.dp)) {
                Text(
                    text = activity.name?: "no name available",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 8f)
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
             }

            Row(modifier = Modifier.align(BottomEnd).padding(16.dp)) {
                IconButton(onClick = { viewModel.activityInteraction("like", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { viewModel.activityInteraction("dislike", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                }
            }

            Row(modifier = Modifier.align(BottomStart).padding(16.dp)) {
                IconButton(onClick = { viewModel.activityInteraction("skip", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "skip")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { viewModel.activityInteraction("visited", activity) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = "already visited")
                }
            }
        }
    }
}

//@Composable
//@Preview
//fun ActivityDetailPreview() {
//    Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = CenterVertically) {
//        ActivityCard(Modifier, detailView = remember { mutableStateOf(false) })
//    }
//}