package com.jetbrains.kmpapp.auth

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jetbrains.kmpapp.MainActivity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

data class User(val id: String, val name: String, val email: String)

//Chat GPT
object AuthViewModel {
    var currentUser by mutableStateOf<User?>(null)
        private set

    var httpClient = HttpClient()


    fun setUser(user: User) {
        currentUser = user
    }

    fun clearUser() {
        currentUser = null
    }

    suspend fun authenticateWithBackend(code: String): Boolean {
        val response = httpClient.post("https://your-backend.com/exchange-code") {
            setBody(mapOf("code" to code))
        }
        setUser(response.body())
        println("${currentUser?.id ?: "no user id"}, ")
        return response.status == HttpStatusCode.OK
    }
}

