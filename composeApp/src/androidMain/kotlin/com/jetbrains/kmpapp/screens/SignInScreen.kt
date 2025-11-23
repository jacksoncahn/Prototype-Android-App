package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SignInScreen(onSignInClick: () -> Unit, signInError: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = onSignInClick, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
            Text("Sign In", color = Color.Black)
        }
        if (signInError != "") {
            Text(signInError, color = Color.Red, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(onSignInClick = {}, "Error Message")
}