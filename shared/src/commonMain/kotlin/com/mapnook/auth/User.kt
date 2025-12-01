package com.mapnook.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapnook.api.ApiClient
import com.mapnook.api.posts.MyPostsViewModel
import kotlinx.coroutines.launch


@Serializable
data class User (
    val id: String?,
    val displayName: String?,
    val bio: String?,
//    val tags: List<JsonObject>?,
    val avatarUrl: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
)

class myUserViewModel: ViewModel() {
    var user by mutableStateOf<User?>(null)

    var internalPostViewModel = MyPostsViewModel()
    
    var tempUserStorage by mutableStateOf<User?>(null)

    fun fetchUser(email: String) {
        viewModelScope.launch {
            try {
                val fetchedUser = ApiClient.getUserByEmailAddress(email)
                tempUserStorage = fetchedUser
                println("tempUserStorage, $tempUserStorage")
            } catch (e: Exception) {
                println("Error fetching user: ${e.message}")
            }
        }
    }
}

