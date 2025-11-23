package com.jetbrains.kmpapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jetbrains.kmpapp.screens.basic.About
import com.jetbrains.kmpapp.screens.basic.AddPlaceOrEvent
import com.jetbrains.kmpapp.screens.basic.Contact
import com.jetbrains.kmpapp.screens.basic.FAQ
import com.jetbrains.kmpapp.screens.basic.Help
import com.jetbrains.kmpapp.screens.basic.Home
import com.jetbrains.kmpapp.screens.basic.MyLists
import com.jetbrains.kmpapp.screens.basic.Settings
import com.jetbrains.kmpapp.screens.trip.Trip
import com.jetbrains.kmpapp.screens.trip.TripList
import com.jetbrains.kmpapp.screens.trip.TripPlanner
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

    val navRoute = remember { mutableStateOf("") }
    val navController = rememberNavController()

    //used for UI changes per-page (currently not working), as well as proper listType for MyLists page

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
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)) {
                NavHost(navController, startDestination = "home") {

                    composable("home") { Home(modifier = Modifier.padding(paddingValues), isLoading = isLoading) }

                    //for a lot of the pages, the "x" (icon to close the page) navigates back home.
                    //the button has an onClick navigation functionality, which is passed in from outside
                    composable(route = "settings") { Settings({navController.navigate("home")}) }
                    composable(route = "faq") { FAQ({navController.navigate("home")}) }
                    composable(route = "about") { About({navController.navigate("home")}) }
                    composable(route = "addplaceorevent") { AddPlaceOrEvent({navController.navigate("home")}) }
                    composable(route = "contact") { Contact({navController.navigate("home")}) }
                    composable(route = "help") {Help({navController.navigate("home")})}
                    composable(route = "triplist") {TripList({route -> { navController.navigate(route) }})}
//
                    composable(
                        route = "mylists/{listType}",
                        arguments = listOf(navArgument("ids") {
                            type = NavType.StringType
                            nullable = true
                        })
                        )
                    { backStackEntry ->
                        val listType = backStackEntry.arguments?.getString("listType")
                        MyLists({ route -> navController.navigate(route) }, listType)
                    }


                    composable(
                        route = "tripplanner?ids={ids}",
                        arguments = listOf(navArgument("ids") {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { backStackEntry ->
                        val ids = backStackEntry.arguments?.getString("ids")
                        TripPlanner(ids, { navController.popBackStack() })
                    }

                    composable(
                        route = "triplist",
                    ) {
                        TripList({ route -> navController.navigate(route) })
                    }

                    composable(
                        route = "trip/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")
                        Trip(id, {navController.navigate("triplist")} )
                    }
                }

                Menu(
                    navController,
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValues)
                        .zIndex(1f),
                )
            }
        }
    }
}
