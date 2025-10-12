package com.jetbrains.kmpapp.components

import android.R.attr.opacity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.jetbrains.kmpapp.R
import kotlin.math.round

@Composable
fun ActivityCard(modifier: Modifier, number: Int, detailView: MutableState<Boolean>) {
    if (detailView.value) {
        ActivityCardLarge(modifier = modifier.padding(bottom = 32.dp), number = number, detailView)
    } else {
        ActivityCardSmall(modifier = modifier.padding(bottom = 32.dp), number = number, detailView)
    }
}

@Composable
fun ActivityCardLarge(modifier: Modifier, number: Int, detailView: MutableState<Boolean>) {
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

                Image(painter = painterResource(id = R.drawable.placeholder_loc), contentDescription = "Location background", modifier = Modifier.height(200.dp).fillMaxWidth(), contentScale = ContentScale.Crop)

                IconButton(onClick = { detailView.value = false }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)).align(TopEnd)) {
                    Icon(Icons.AutoMirrored.Filled.CallReceived, contentDescription = "Enlarge")
                }

                Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 8.dp).background(color = Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))) {
                    Text(text = "Title English", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
                    Text(text = "Title Czech", modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp))
                }

                Row(modifier = Modifier.align(BottomEnd).padding(8.dp)) {
                    Row() {
                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                            Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                        }
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Row() {
                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                            Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                        }
                    }
                }

                Row(modifier = Modifier.align(BottomStart).padding(8.dp)) {
                    Row() {
                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                            Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "want to go")
                        }
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Row() {
                        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                            Icon(Icons.Default.RemoveRedEye, contentDescription = "not for me")
                        }
                    }
                }
            }

            Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis sem at magna sodales consectetur. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer hendrerit dui vel lorem scelerisque, sed venenatis leo ullamcorper. Fusce efficitur tristique risus sed varius. Sed facilisis suscipit neque sit amet commodo. Fusce sagittis augue tincidunt velit bibendum, quis commodo urna iaculis. Aliquam bibendum nunc blandit massa molestie, sit amet porta massa facilisis. Praesent sed congue lectus.", modifier = Modifier.padding(16.dp))

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
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SCROLLTEST")
            }




        }
    }
}

@Composable
fun ActivityCardSmall(modifier: Modifier, number: Int, detailView: MutableState<Boolean>) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )

    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Image(painter = painterResource(id = R.drawable.placeholder_loc), contentDescription = "Location background", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

            IconButton(onClick = { detailView.value = true }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)).align(TopEnd)) {
                Icon(Icons.AutoMirrored.Filled.CallMade, contentDescription = "Enlarge")
            }

            Row(modifier = Modifier.align(BottomEnd).padding(8.dp)) {
                Row() {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "want to go")
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Row() {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "not for me")
                    }
                }
            }

            Row(modifier = Modifier.align(BottomStart).padding(8.dp)) {
                Row() {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "want to go")
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Row() {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = "not for me")
                    }
                }
            }

            Column(horizontalAlignment = Start, modifier = Modifier.padding(all = 16.dp).background(color = Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))) {
                Text(text = "Title English", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 4.dp).padding(top = 4.dp))
                Text(text = "Title Czech", modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp))
            }
        }


    }
}

@Composable
@Preview
fun ActivityDetailPreview() {
    Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = CenterVertically) {
        ActivityCard(Modifier, 1, detailView = remember { mutableStateOf(false) })
    }
}