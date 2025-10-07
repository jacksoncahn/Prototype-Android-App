package com.jetbrains.kmpapp

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetbrains.kmpapp.screens.Home
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(navController, startDestination = "home") {
                    composable("home") { Home() }
                    composable("settings") { Settings()}
                }

                Menu(
                    navRoute,
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 8.dp, vertical = 24.dp)
                        .zIndex(1f)
                )
            }
    }
}
