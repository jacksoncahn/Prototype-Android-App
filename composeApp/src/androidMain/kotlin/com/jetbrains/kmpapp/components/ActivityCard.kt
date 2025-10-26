package com.jetbrains.kmpapp.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.jetbrains.kmpapp.MainActivity
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.Post

@Composable
fun ActivityCard(modifier: Modifier, detailView: MutableState<Boolean>, post: Post) {
    if (detailView.value) {
        ActivityCardLarge(modifier = modifier.padding(bottom = 32.dp), detailView, post)
    } else {
        ActivityCardSmall(modifier = modifier.padding(bottom = 32.dp), detailView, post)
    }
}

@Composable
fun ActivityCardLarge(modifier: Modifier, detailView: MutableState<Boolean>, post: Post) {

    //should access shared viewModel for posts
    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .padding(all = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth()) {

                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                
                IconButton(onClick = { detailView.value = false }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp)).align(TopEnd)) {
                    Icon(Icons.AutoMirrored.Filled.CallReceived, contentDescription = "Enlarge")
                }

                Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 8.dp).background(color = Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))) {
                    Text(text = (post.name?: "no name available"), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
                    Text(text = (post.nativeName?: "no native name available"), modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp))
                }

                Row(modifier = Modifier.align(BottomEnd).padding(16.dp)) {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                    }
                }

                Row(modifier = Modifier.align(BottomStart).padding(16.dp)) {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "want to go")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = "not for me")
                    }
                }
            }

            Text(text = (post.description?: "no description available"), modifier = Modifier.padding(16.dp))

            Column() {
                Text("Tags", fontSize = MaterialTheme.typography.titleMedium.fontSize, modifier = Modifier.padding(all = 16.dp))
                FlowRow {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text("tag1******", modifier = Modifier.padding(8.dp).background(color = Color.Gray, shape = RoundedCornerShape(8.dp)))
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Links")
                Text("this is not a real link")
            }
        }
    }
}

@Composable
fun ActivityCardSmall(modifier: Modifier, detailView: MutableState<Boolean>, post: Post) {

    //should access shared viewModel for posts
    val viewModel: MyPostsViewModel = viewModel(
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
                model = post.imageUrl,
                contentDescription = post.name,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            IconButton(onClick = { detailView.value = true }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp)).align(TopEnd)) {
                Icon(Icons.AutoMirrored.Filled.CallMade, contentDescription = "Enlarge")
            }

            Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 16.dp).background(color = Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))) {
                Text(text = post.name?: "no name available", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
                Text(text = post.nativeName?: "no native name available", modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp))
            }

            Row(modifier = Modifier.align(BottomEnd).padding(16.dp)) {
                IconButton(onClick = { viewModel.activityInteraction("like", post) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { viewModel.activityInteraction("dislike", post) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                }
            }

            Row(modifier = Modifier.align(BottomStart).padding(16.dp)) {
                IconButton(onClick = { viewModel.activityInteraction("skip", post) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "skip")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { viewModel.activityInteraction("visited", post) }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))) {
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