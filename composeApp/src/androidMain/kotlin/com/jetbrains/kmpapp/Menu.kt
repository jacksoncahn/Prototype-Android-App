package com.jetbrains.kmpapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource


@Composable
fun MenuContent(navigation: MutableState<String>) {
    Column(modifier = Modifier.background(color = Color.Black, shape = RoundedCornerShape(8.dp)).width(IntrinsicSize.Max).padding(vertical = 8.dp)) {

        Row(modifier = Modifier.clickable { navigation.value = "settings" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text("Account Name", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text("Account Settings", color = Color.LightGray)
            }

        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

        Column(modifier = Modifier.clickable { navigation.value = "mylists" }.padding(8.dp)) {
            Text("My Lists", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.clickable { navigation.value = "wanttogo" }.padding(vertical = 8.dp)) {
                Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Want to go", tint = Color.White)
                Spacer(modifier = Modifier.size(16.dp))
                Text("Want to go", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
            Row(modifier = Modifier.clickable { navigation.value = "visited" }.padding(vertical = 8.dp)) {
                Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Visited", tint = Color.White)
                Spacer(modifier = Modifier.size(16.dp))
                Text("Visited", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

        Row(modifier = Modifier.clickable { navigation.value = "addplaceorevent" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Text("Add place or event", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

        Row(modifier = Modifier.clickable { navigation.value = "faq" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Text("FAQ", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        Row(modifier = Modifier.clickable { navigation.value = "help" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Text("Help", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        Row(modifier = Modifier.clickable { navigation.value = "contact" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Text("Contact", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        Row(modifier = Modifier.clickable { navigation.value = "about" }.padding(all = 8.dp)) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Text("About", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun Menu(navigation: MutableState<String>, page: MutableState<String>, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        println("Route changed, navigation value: ${page.value}")
    }
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(horizontal = 8.dp)) {
        IconButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.size(56.dp).background(
                color = if (page.value == "home") Color.Black else Color(0xFF1F1F1F), shape = CircleShape
            )) {
            Icon(painter = painterResource(R.drawable.logo), contentDescription = "Menu", modifier = Modifier.size(32.dp), tint = Color.White)
        }

        if (showMenu) {
            Popup(
                alignment = TopStart,
                offset = IntOffset(0, 160.dp.value.toInt()),
                onDismissRequest = { showMenu = false }
            ) {
                MenuContent(navigation)
            }
        }
    }
}

//@Composable
//@Preview
//fun MenuPreview() {
//    Menu(remember { mutableStateOf("") })
//}
