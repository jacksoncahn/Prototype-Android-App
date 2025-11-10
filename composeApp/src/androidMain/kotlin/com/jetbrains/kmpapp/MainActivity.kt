package com.jetbrains.kmpapp

//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.core.net.toUri
//import androidx.lifecycle.lifecycleScope
//import com.jetbrains.kmpapp.screens.SignInScreen
//import kotlinx.coroutines.launch
//import com.jetbrains.kmpapp.auth.AuthViewModel
//
//
//class MainActivity : ComponentActivity() {
//
//    private var showSignIn = mutableStateOf(true)
//    var signInError = mutableStateOf("")
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            // Remove when https://issuetracker.google.com/issues/364713509 is fixed
//
//            //Chat GPT
////            setContent {
////                val signedIn = showSignIn.value
////
////                if (signedIn) {
////                    SignInScreen(onSignInClick = { startLoginFlow()}, signInError = signInError.value)
////                } else {
////
////                }
//            LaunchedEffect(isSystemInDarkTheme()) {
//                enableEdgeToEdge()
//            }
//            App()
//            }
//        }
//    }
//
////    private fun startLoginFlow() {
////        val clientID = BuildConfig.WORKOS_CLIENT_ID
////
////        val loginUri = "https://api.workos.com/sso/authorize?client_id=$clientID&redirect_uri=kmpapp://callback&response_type=code"
////        val intent = Intent(Intent.ACTION_VIEW, loginUri.toUri())
////        startActivity(intent)
////    }
//
////    override fun onNewIntent(intent: Intent) {
////        super.onNewIntent(intent)
////        val data = intent.data
////        if (data != null && data.toString().startsWith("kmpapp://callback")) {
////            val code = data.getQueryParameter("code")
////            if (code != null) {
////                // Launch an asynchronous coroutine to handle the login
////                lifecycleScope.launch {
////                    val success = AuthViewModel.authenticateWithBackend(code) // suspend function
////                    if (success) {
////                        showSignIn.value = false
////                    } else {
////                        signInError.value = "Sign In Failed"
////                        showSignIn.value = true
////                    }
////                }
////            }
////        }
////    }
//
//