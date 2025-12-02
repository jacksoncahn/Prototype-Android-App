package com.jetbrains.kmpapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import com.jetbrains.kmpapp.screens.auth.SignInScreen


class MainActivity : ComponentActivity() {

    var showSignIn = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

//COMMENTED SIGNIN SCREEN LOGIC OUT FOR QUICKER TESTING

//            LaunchedEffect(showSignIn) {
//                println("showSignIn, $showSignIn")
//            }
////            // Remove when https://issuetracker.google.com/issues/364713509 is fixed
//            if (showSignIn.value) {
//                SignInScreen(
//                    onSignInClick = {showSignIn.value = false},
//                )
//            } else if (!showSignIn.value) {
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }
            App()
//        }
        }
    }
//    private fun startLoginFlow() {
//
//        val clientID = BuildConfig.WORKOS_CLIENT_ID
//        val apiKey = BuildConfig.WORKOS_API_KEY
//        val redirectUri = "kmpapp://callback"
//
//        val loginUrl = buildString {
//            append("https://api.workos.com/user_management/authorize")
//            append("?client_id=$clientID")
//            append("&provider=authkit")
//            append("&redirect_uri=$redirectUri")
//            append("&response_type=code")
//            append("&ts=${System.currentTimeMillis()}") // optional: force unique query to bypass caching
//        }
//
//        val customTabsIntent = CustomTabsIntent.Builder().build()
//        customTabsIntent.launchUrl(this, loginUrl.toUri())
//    }

//    fun startWorkOSLoginFlow(context: Context) {
//        println("WORKOSAPI call started")
//        val redirectUri = "kmpapp://callback"
//        val orgID = "org_01JVCMYS22WK8ENJW98YGH2T29"
//
//        Thread {
//            try {
//                val clientID = BuildConfig.WORKOS_CLIENT_ID
//
//                val urlString = buildString {
//                    append("https://api.workos.com/user_management/authorization_sessions")
//                    append("?client_id=").append(clientID)
//                    append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"))
//                    append("&organization=").append(orgID)
//                }
//
//                val url = URL(urlString)
//                println("URL, $url")
//
//                val conn = (url.openConnection() as HttpsURLConnection).apply {
//                    requestMethod = "GET"
//                    // DO NOT add authorization header (AuthKit does not use it)
//                }
//
////                val response = conn.inputStream.bufferedReader().readText()
////                conn.disconnect()
//
//                val responseText = try {
//                    conn.inputStream.bufferedReader().readText()
//                } catch (e: Exception) {
//                    conn.errorStream?.bufferedReader()?.readText() ?: "No error body"
//                }
//
//                println("AUTHKIT RESPONSE: $responseText")
//
//                val loginUrl = JSONObject(responseText).getString("url")
//
//                (context as Activity).runOnUiThread {
//                    val customTabsIntent = CustomTabsIntent.Builder().build()
//                    customTabsIntent.launchUrl(context, loginUrl.toUri())
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }.start()
//    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        val data = intent.data
//        if (data != null && data.toString().startsWith("kmpapp://callback")) {
//            val code = data.getQueryParameter("code")
//            if (code != null) {
//                // Launch an asynchronous coroutine to handle the login
//                lifecycleScope.launch {
//                    val success = AuthViewModel.authenticateWithBackend(code) // suspend function
//                    if (success) {
//                        showSignIn.value = false
//                    } else {
//                        signInError.value = "Sign In Failed"
//                        showSignIn.value = true
//                    }
//                }
//            }
//        }
    }
}