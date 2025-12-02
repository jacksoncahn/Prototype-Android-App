package com.jetbrains.kmpapp.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jetbrains.kmpapp.R
import com.mapnook.auth.myUserViewModel

@Composable
fun Menu(navController: NavController, modifier: Modifier = Modifier) {

    val userViewModel: myUserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val user = userViewModel.user

    //the entire following body of logic is just to switch menu button color
    val currentRoute = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        currentRoute.value = "home"
    }
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry?.destination?.route?.let {
            currentRoute.value = it
        }
    }

    //set
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(horizontal = 8.dp)) {
        IconButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.size(56.dp).background(
                color = if (currentRoute.value == "home") Color.Black else Color(0xFF1F1F1F), shape = CircleShape
            )) {
            Icon(painter = painterResource(R.drawable.logo), contentDescription = "Menu", modifier = Modifier.size(32.dp), tint = Color.White)
        }

        if (showMenu) {
            Popup(
                alignment = TopStart,
                offset = IntOffset(0, 160.dp.value.toInt()),
                onDismissRequest = { showMenu = false }
            ) {
                Column(modifier = Modifier.background(color = Color.Black, shape = RoundedCornerShape(8.dp)).width(IntrinsicSize.Max).padding(vertical = 8.dp)) {

                    Row(modifier = Modifier.clickable { navController.navigate("settings") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))

                        Column {
                            if (user?.displayName != null) {
                                Text(user.displayName!!, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        } else {
//                                Text("Account Name", color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Text("Account Settings", color = Color.LightGray)
                            }
                        }

                    }

                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Text("My Lists", color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 8.dp))

                    Column(modifier = Modifier.clickable { navController.navigate("mylists/wanttogo") }.padding(start = 16.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.clickable { navController.navigate("mylists/wanttogo") }.padding(vertical = 8.dp)) {
                            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Want to go", tint = Color.White)
                            Spacer(modifier = Modifier.size(16.dp))
                            Text("Want to go", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        }
                        Row(modifier = Modifier.clickable { navController.navigate("mylists/visited") }.padding(vertical = 8.dp)) {
                            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Visited", tint = Color.White)
                            Spacer(modifier = Modifier.size(16.dp))
                            Text("Visited", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Row(modifier = Modifier.clickable { navController.navigate("triplist") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("My Trips", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    // New feature Brett is working on. Might make more organizational sense.
                    Row(modifier = Modifier.clickable { navController.navigate("plantrip") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("Plan Trip", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

                    Row(modifier = Modifier.clickable { navController.navigate("addplaceorevent") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("Add place or event", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

                    Row(modifier = Modifier.clickable { navController.navigate("faq") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("FAQ", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    Row(modifier = Modifier.clickable { navController.navigate("help") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("Help", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    Row(modifier = Modifier.clickable { navController.navigate("contact") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("Contact", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }

                    Row(modifier = Modifier.clickable { navController.navigate("about") }.padding(all = 8.dp)) {
                        Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Menu", tint = Color.White)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("About", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }
                }

            }
        }
    }
}

