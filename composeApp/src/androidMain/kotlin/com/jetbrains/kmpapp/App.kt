package com.jetbrains.kmpapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bitmapDescriptorFromUrl
import com.jetbrains.kmpapp.screens.About
import com.jetbrains.kmpapp.screens.AddPlaceOrEvent
import com.jetbrains.kmpapp.screens.Contact
import com.jetbrains.kmpapp.screens.FAQ
import com.jetbrains.kmpapp.screens.About
import com.jetbrains.kmpapp.screens.AddPlaceOrEvent
import com.jetbrains.kmpapp.screens.Contact
import com.jetbrains.kmpapp.screens.FAQ
import com.jetbrains.kmpapp.screens.Home
import com.jetbrains.kmpapp.screens.MyLists
import com.jetbrains.kmpapp.screens.MyLists
import com.jetbrains.kmpapp.screens.Settings
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.Post

//test comment for committing and pushing
//@SuppressLint("UnrememberedMutableState")
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App() {

    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    var isLoading = remember { mutableStateOf(true) }

    //fetches posts when component loads
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            viewModel.fetchPosts()
            isLoading.value = false
            if (viewModel.posts.isNotEmpty()) {
            }
        } catch(e: Exception) {
            Log.e("App", "Error fetching posts")
        }
    }

    var navRoute = remember { mutableStateOf("") }

    val navController = rememberNavController()

    LaunchedEffect(navRoute.value) {
        if (navRoute.value.isNotEmpty()) {
            navController.navigate(navRoute.value)
            // Optional: Reset the navRoute so it can be triggered again
            navRoute.value = ""
        }
    }

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(navController, startDestination = "home") {
                    composable("home") { Home(modifier = Modifier.padding(innerPadding), isLoading = isLoading) }
                    composable("settings") { Settings()}
                    composable(route = "faq") { FAQ() }
                    composable(route = "about") {About()}
                    composable(route = "addplaceorevent") { AddPlaceOrEvent() }
                    composable(route = "contact") { Contact() }
                    composable("mylists") { MyLists(navController)} // Pass the NavController here
                }

                Menu(
                    navRoute,
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(innerPadding)
                        .zIndex(1f),
                )
            }
        }

    }
}
