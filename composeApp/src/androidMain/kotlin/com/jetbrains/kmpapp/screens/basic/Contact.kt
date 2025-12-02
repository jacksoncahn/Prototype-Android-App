package com.jetbrains.kmpapp.screens.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Contact(onClose: () -> Unit) { // Accept the ViewModel
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) { // Use a Box to allow overlaying buttons
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 26.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Contact Us",
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}