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
import com.jetbrains.kmpapp.components.Menu
import com.jetbrains.kmpapp.screens.basic.About
import com.jetbrains.kmpapp.screens.basic.AddPlaceOrEvent
import com.jetbrains.kmpapp.screens.basic.Contact
import com.jetbrains.kmpapp.screens.basic.FAQ
import com.jetbrains.kmpapp.screens.basic.Help
import com.jetbrains.kmpapp.screens.basic.Home
import com.jetbrains.kmpapp.screens.basic.MyLists
import com.jetbrains.kmpapp.screens.trip.PlanTrip
import com.jetbrains.kmpapp.screens.basic.Settings
import com.jetbrains.kmpapp.screens.trip.Trip
import com.jetbrains.kmpapp.screens.trip.TripList
import com.jetbrains.kmpapp.screens.trip.TripPlanner
import com.mapnook.api.activities.ActivitiesViewModel
import com.mapnook.api.user.UserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {

    //initialize MyPostsViewModel
    val viewModel: ActivitiesViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    val userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    LaunchedEffect(Unit) {
        coroutineScope {
            launch { userViewModel.fetchUserTrips() }
            launch { userViewModel.fetchUserActions("yes") }
            launch { userViewModel.fetchUserActions("no") }
            launch { userViewModel.fetchUserActions("skipped") }
            launch { userViewModel.fetchUserActions("visited") }
        }
    }


    //set isLoading to true while fetching posts (used inside Home.kt to decided when posts are okay to display)
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            viewModel.fetchNewActivities(userViewModel.user!!.id!!)
            isLoading.value = false
        } catch(e: Exception) {
            Log.e("App", "Error fetching posts")
        }
    }

    //initialize navController for routing between pages
    val navController = rememberNavController()

    //App.kt content
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController, startDestination = "home") {

                    //pass in isLoading
                    composable("home") { Home(modifier = Modifier.padding(paddingValues), isLoading = isLoading) }

                    composable("settings") { Settings(onClose = { navController.popBackStack() }) }
                    composable("faq") { FAQ(onClose = { navController.popBackStack() }) }
                    composable("about") { About(onClose = { navController.popBackStack() }) }
                    composable("addplaceorevent") { AddPlaceOrEvent(onClose = { navController.popBackStack() }) }
                    composable("contact") { Contact(onClose = { navController.popBackStack() }) }
                    composable("help") { Help(onClose = { navController.popBackStack() }) }

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
                    composable("plantrip") {
                        PlanTrip(
                            onClose = { navController.popBackStack() },
                            navigateTo = { route -> navController.navigate(route) }
                        )
                    }
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

                //imported Menu component from Menu.kt
                Menu(
                    navController = navController,
                    modifier = Modifier.align(Alignment.TopStart).padding(paddingValues).zIndex(1f)
                )
            }
        }
    }
}
