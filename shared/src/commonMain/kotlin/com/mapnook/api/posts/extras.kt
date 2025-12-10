package com.mapnook.api.posts

import androidx.lifecycle.viewModelScope
import com.mapnook.api.ApiClient
import kotlinx.coroutines.launch
import kotlin.collections.plus

suspend fun fetchActivity(activityId: String): Activity? {
        try {
            val activity = ApiClient.fetchActivity(activityId)
            return activity

        } catch (e: Exception) {
            println("Error fetching activity: ${e.message}")
            return null
        }
}

