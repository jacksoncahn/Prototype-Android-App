package com.jetbrains.kmpapp

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.components.Searchbar

//test comment for committing and pushing
@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        // 1. Use Scaffold for a standard Material Design layout structure.
        // It automatically handles safe areas for system UI (status bar, navigation bar).
        Scaffold { innerPadding ->
            // A Box can be used as a blank container.
            // Apply the padding provided by Scaffold to respect the safe areas.
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize() // Fill the available space
                    .padding(16.dp)
            ) {
                Searchbar()
            }
        }
    }
}
