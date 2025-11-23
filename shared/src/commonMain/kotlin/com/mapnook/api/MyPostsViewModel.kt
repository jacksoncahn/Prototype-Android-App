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

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
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

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val fetchedPosts = ApiClient.getPosts()
                posts = fetchedPosts
                visiblePosts = fetchedPosts
                if (fetchedPosts.isNotEmpty()) {
                    selectedPost = visiblePosts[0]
                }
                println("post: $selectedPost")
            } catch (e: Exception) {
                println("Error fetching posts: ${e.message}")
            }
        }
    }

    fun createTrip(name: String, posts: List<Post>) {
        // Generate a new unique ID for the trip
        val newId = (trips.maxOfOrNull { it.id } ?: 0) + 1
        val newTrip = Trip(id = newId, name = name, posts = posts)
        trips = trips + newTrip
    }

    fun deleteTrip(tripId: Int) {
        trips = trips.filterNot { it.id == tripId }
    }

    fun activityInteraction(action: String, post: Post) {
        println("post name ${post.name}")
        when (action) {
            "visited" -> {
                if (!post.visited) {
                    post.visited = true
                    visited += post
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
        if (visiblePosts.isNotEmpty()) {
            selectedPost = visiblePosts[0]
        } else {
            selectedPost = null
        }
    }
}
