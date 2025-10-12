package com.jetbrains.kmpapp

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
//test comment for committing and pushing
//@SuppressLint("UnrememberedMutableState")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App() {

    var navRoute = remember { mutableStateOf("") }

    val navController = rememberNavController()

//    var menuButtonCoords = remember { mutableStateOf<LayoutCoordinates?>(null) }

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
                    composable("home") { Home(modifier = Modifier.padding(innerPadding)) }
                    composable("settings") { Settings()}
                    composable(route = "faq") { FAQ() }
                    composable(route = "about") {About()}
                    composable(route = "addplaceorevent") { AddPlaceOrEvent() }
                    composable(route = "contact") { Contact() }
                    composable("settings") { Settings() }
                    composable("mylists") { MyLists(navController) } // Pass the NavController here
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
