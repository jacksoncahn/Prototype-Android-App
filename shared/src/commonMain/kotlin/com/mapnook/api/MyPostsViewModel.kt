package com.mapnook.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyPostsViewModel : ViewModel() {

    data class Trip(
        val id: Int,
        val name: String,
        val posts: List<Post>,
        var baseLoc: List<Double>? = null,
        var baseName: String? = null,
        var baseAddress: String? = null
    )

    // 1. Create a state to hold the list of posts for your UI.
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set // Make it read-only from the outside
    var visiblePosts by mutableStateOf<List<Post>>(emptyList())

    var visited by mutableStateOf<List<Post>>(emptyList())
        private set
    var wanttogo by mutableStateOf<List<Post>>(emptyList())
        private set

    var skipped by mutableStateOf<List<Post>>(emptyList())
        private set

    var notforme by mutableStateOf<List<Post>>(emptyList())
        private set

    var selectedPost by mutableStateOf<Post?>(null)

    var trips by mutableStateOf<List<Trip>>(emptyList())

    init {
        fetchPosts()
    }


    // 2. A function to trigger the network request.
    fun fetchPosts() {
        // 3. Launch a coroutine in the viewModelScope.
        viewModelScope.launch {
            try {
                val fetchedPosts = ApiClient.getPosts()

                posts = fetchedPosts
                visiblePosts = fetchedPosts
                selectedPost = visiblePosts[0]
                println("post: $selectedPost")
            } catch (e: Exception) {
                // It's crucial to handle potential errors,
                // like no internet connection.
                println("Error fetching posts: ${e.message}")
            }
        }
    }

    fun activityInteraction(action: String, post: Post) {
//        println("activityInteraction called with action: $action and post: ${post.name}, liked: ${post.liked}, visited: ${post.visited}, skipped: ${post.skipped}, disliked: ${post.disliked}")
        println("post name ${post.name}")
        when (action) {
            "visited" -> {
                if (!post.visited) {
                    post.visited = true
                    visited += post
                    println("visited: ${visited.size}")
                }
                visiblePosts -= post
            }
            "like" -> {
                if (!post.liked) {
                    post.liked = true
                    wanttogo += post
                }
                visiblePosts -= post
            }
            "skip" -> {
                if (!post.skipped) {
                    post.skipped = true
                    skipped += post
                }
                visiblePosts -= post
            }
            "dislike" -> {
                if (!post.disliked) {
                    post.disliked = true
                    notforme += post
                }
                visiblePosts -= post
            }
        }
        if (!visiblePosts.isEmpty()) {
            selectedPost = visiblePosts[0]
        } else {
            selectedPost = null
        }
        }
    }

