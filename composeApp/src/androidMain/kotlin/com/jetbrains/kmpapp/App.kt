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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jetbrains.kmpapp.screens.About
import com.jetbrains.kmpapp.screens.AddPlaceOrEvent
import com.jetbrains.kmpapp.screens.Contact
import com.jetbrains.kmpapp.screens.FAQ
import com.jetbrains.kmpapp.screens.Home
import com.jetbrains.kmpapp.screens.MyLists
import com.jetbrains.kmpapp.screens.Settings
import com.jetbrains.kmpapp.screens.TripPlanner
import com.mapnook.api.MyPostsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    var isLoading = remember { mutableStateOf(true) }

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
            navRoute.value = ""
        }
    }

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController, startDestination = "home") {
                    composable("home") { Home(modifier = Modifier.padding(paddingValues), isLoading = isLoading) }
                    composable("settings") { Settings() }
                    composable("faq") { FAQ() }
                    composable("about") { About() }
                    composable("addplaceorevent") { AddPlaceOrEvent() }
                    composable("contact") { Contact() }
                    composable("mylists") { MyLists(navController, viewModel) } 
                    composable(
                        route = "tripplanner?ids={ids}",
                        arguments = listOf(navArgument("ids") {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { backStackEntry ->
                        val ids = backStackEntry.arguments?.getString("ids")
                        // Pass the navController to the TripPlanner screen
                        TripPlanner(ids, viewModel, navController)
                    }
                }

                Menu(
                    navRoute,
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValues)
                        .zIndex(1f),
                )
            }
        }
    }
}
