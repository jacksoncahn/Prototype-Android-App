package com.mapnook.api.posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapnook.api.ApiClient
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActivitiesViewModel : ViewModel() {
    var activities by mutableStateOf<List<Activity>>(emptyList())
        private set
    var visibleActivities by mutableStateOf<List<Activity>>(emptyList())
    var visited by mutableStateOf<List<Activity>>(emptyList())
        private set
    var wanttogo by mutableStateOf<List<Activity>>(emptyList())
        private set
    var skipped by mutableStateOf<List<Activity>>(emptyList())
        private set
    var notforme by mutableStateOf<List<Activity>>(emptyList())
        private set
    var selectedActivity by mutableStateOf<Activity?>(null)


    fun fetchNewActivities(userId: String) {
        viewModelScope.launch {
            try {
                val fetchedPosts = ApiClient.getNewActivities(userId)
                activities = fetchedPosts
                visibleActivities = fetchedPosts
                if (fetchedPosts.isNotEmpty()) {
                    selectedActivity = visibleActivities[0]
                }
                println("post: $selectedActivity")
            } catch (e: Exception) {
                println("Error fetching activities: ${e.message}")
            }
        }
    }

    //in the future, access db by calling a function in API Client
    fun activityInteraction(action: String, activity: Activity) {
        println("post name ${activity.name}")
        when (action) {
            "visited" -> {
                if (!activity.visited) {
                    activity.visited = true
                    visited += activity
                }
                visibleActivities -= activity
            }
            "like" -> {
                if (!activity.liked) {
                    activity.liked = true
                    wanttogo += activity
                }
                visibleActivities -= activity
            }
            "skip" -> {
                if (!activity.skipped) {
                    activity.skipped = true
                    skipped += activity
                }
                visibleActivities -= activity
            }
            "dislike" -> {
                if (!activity.disliked) {
                    activity.disliked = true
                    notforme += activity
                }
                visibleActivities -= activity
            }
        }
        if (visibleActivities.isNotEmpty()) {
            selectedActivity = visibleActivities[0]
        } else {
            selectedActivity = null
        }
    }

    fun removeFromWantToGo(id: String) {
        wanttogo = wanttogo.filterNot { it.id == id }
    }

    fun removeFromVisited(id: String) {
        visited = visited.filterNot { it.id == id }
    }

    fun removeFromSkipped(id: String) {
        skipped = skipped.filterNot { it.id == id }
    }

    fun removeFromNotForMe(id: String) {
        notforme = notforme.filterNot { it.id == id }
    }
}
