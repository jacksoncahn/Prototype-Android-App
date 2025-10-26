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
    var visited by mutableStateOf<List<Post>>(emptyList())
        private set
    var wanttogo by mutableStateOf<List<Post>>(emptyList())
        private set

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

    fun activityInteraction(action: String, post: Post) {
        when (action) {
            "visited" -> {
                visited += post
                posts -= post
            }
            "like" -> {
                wanttogo += post
                posts -= post
            }
            "skip" -> {
                //hopefully this triggers the UI to reload and it updates accordingly
                posts -= post
            }
            "dislike" -> {
                //hopefully this triggers the UI to reload and it updates accordingly
                posts -= post
            }
        }
    }
}