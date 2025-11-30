package com.jetbrains.kmpapp.screens.auth

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.auth.myUserViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SignInScreen(onSignInClick: () -> Unit, signInError: String) {
    var typedInString by rememberSaveable { mutableStateOf("") }

    var showSignInButton by remember {mutableStateOf(false)}

    val userViewModel: myUserViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )

    var emailSearchAttempted by remember { mutableStateOf(false) }

    LaunchedEffect(emailSearchAttempted) {
        userViewModel.fetchUser(email = typedInString)
        emailSearchAttempted = false
    }

    LaunchedEffect(userViewModel.tempUserStorage) {
        showSignInButton = userViewModel.tempUserStorage != null
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = typedInString,
            onValueChange = { typedInString = it},
            label = { Text("Search by email") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (showSignInButton) {
            Button(onClick = {
                onSignInClick()
                userViewModel.user = userViewModel.tempUserStorage
                userViewModel.tempUserStorage = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("Sign In", color = Color.Black)
            }

//            Button(onClick = {onSignInClick; userViewModel.user = userViewModel.tempUserStorage; println("testprint")}, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
//                Text("Sign In", color = Color.Black)
//            }

        } else if (userViewModel.tempUserStorage == null && typedInString != "") {
            Button(onClick = {emailSearchAttempted = true}, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("Search for user by email", color = Color.Black)
            }
        }

        if (signInError != "") {
            Text(signInError, color = Color.Red)
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(onSignInClick = {}, "Error Message")
}