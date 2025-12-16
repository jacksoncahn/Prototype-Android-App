package com.mapnook.api.activities

import com.mapnook.api.ApiClient

suspend fun fetchActivity(activityId: String): Activity? {
        try {
            val activity = ApiClient.fetchActivity(activityId)
            return activity

        } catch (e: Exception) {
            println("Error fetching activity: ${e.message}")
            return null
        }
}

