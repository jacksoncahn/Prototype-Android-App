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
import com.jetbrains.kmpapp.screens.Trip
import com.jetbrains.kmpapp.screens.TripList
import com.jetbrains.kmpapp.screens.TripPlanner
import com.mapnook.api.MyPostsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val viewModel: MyPostsViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            viewModel.fetchPosts()
            isLoading.value = false
        } catch(e: Exception) {
            Log.e("App", "Error fetching posts")
        }
    }

    val navController = rememberNavController()

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController, startDestination = "home") {
                    composable("home") { Home(modifier = Modifier.padding(paddingValues), isLoading = isLoading) }
                    composable("settings") { Settings(onClose = { navController.popBackStack() }) }
                    composable("faq") { FAQ(onClose = { navController.popBackStack() }) }
                    composable("about") { About(onClose = { navController.popBackStack() }) }
                    composable("addplaceorevent") { AddPlaceOrEvent(onClose = { navController.popBackStack() }) }
                    composable("contact") { Contact(onClose = { navController.popBackStack() }) }
                    composable(
                        route = "mylists/{listType}",
                        arguments = listOf(navArgument("listType") { type = NavType.StringType })
                    ) { backStackEntry ->
                        MyLists(
                            navigateTo = { route -> navController.navigate(route) },
                            listType = backStackEntry.arguments?.getString("listType")
                        )
                    }
                    composable("triplist") { TripList { route -> navController.navigate(route) } }
                    composable(
                        route = "tripplanner?ids={ids}",
                        arguments = listOf(navArgument("ids") {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { backStackEntry ->
                        val ids = backStackEntry.arguments?.getString("ids")
                        TripPlanner(
                            ids = ids,
                            onTripSaved = {
                                navController.navigate("triplist") { 
                                    popUpTo("triplist") { inclusive = true } 
                                }
                            },
                            popBackStack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "trip/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        Trip(
                            id = backStackEntry.arguments?.getString("id"),
                            onClose = { navController.popBackStack() }
                        )
                    }
                }
                Menu(
                    navController = navController,
                    modifier = Modifier.align(Alignment.TopStart).padding(paddingValues).zIndex(1f)
                )
            }
        }
    }
}
