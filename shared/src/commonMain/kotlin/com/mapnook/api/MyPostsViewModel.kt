package com.mapnook.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyPostsViewModel : ViewModel() {

    // 1. Create a state to hold the list of posts for your UI.
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set // Make it read-only from the outside

    // 2. A function to trigger the network request.
    fun fetchPosts() {
        // 3. Launch a coroutine in the viewModelScope.
        viewModelScope.launch {
            try {
                val fetchedPosts = ApiClient.getPosts()
                posts = fetchedPosts
            } catch (e: Exception) {
                // It's crucial to handle potential errors,
                // like no internet connection.
                println("Error fetching posts: ${e.message}")
            }
        }
    }
}