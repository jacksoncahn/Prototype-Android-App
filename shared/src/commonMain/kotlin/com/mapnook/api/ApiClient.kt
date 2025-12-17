package com.mapnook.api

import com.mapnook.api.activities.Activity
import com.mapnook.api.trips.Trip
import com.mapnook.api.user.User
import com.mapnook.api.user.UserAction
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath
import io.ktor.http.encodedPath
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    //new activities fetched for home page
    suspend fun getNewActivities(userId: String): List<Activity> {
        //production url
        val response = client.get("https://dbcopy-backend.vercel.app/newactivities/${userId}")
        println("Response From Api: ${response.bodyAsText()}")
        val data: List<Activity> = response.body()

        for (post in data) {
            println("post: ${post.name}, ${post.id}, ${post.tags}")
            if (post.primaryImageId != null) {
                post.imageUrl = getImageUrl(post.primaryImageId, 300, 300)
            }
        }
        return data
    }

    //fetch a specific activity by id
    suspend fun fetchActivity(activityId: String): Activity {

        val response = client.get("https://dbcopy-backend.vercel.app/activities/$activityId")
        val responseBodyText = response.bodyAsText()
        println("fetch Single Activity response body: $responseBodyText")
        val data: Activity = response.body()
        if (data.primaryImageId != null) {
            data.imageUrl = getImageUrl(data.primaryImageId, 300, 300)
        }
        return data
    }

    suspend fun saveUserAction(userId: String, activityId: String, type: String) {
        val jsonBody = buildJsonObject {
            put("userId", userId)
            put("activityId", activityId)
            put("type", type)
        }

        val response = client.post("https://dbcopy-backend.vercel.app/saveuseraction/$userId") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(jsonBody))
        }
        println("saveUserAction Response: ${response.status}, ${response.bodyAsText()}")
    }

    suspend fun deleteUserAction(userId: String, activityId: String, type: String) {
        val response =
            client.get("https://dbcopy-backend.vercel.app/removeuseraction/$userId/$activityId/$type")
        println("deleteUserAction Response: ${response.status}, ${response.bodyAsText()}")
    }

    suspend fun fetchUserAction(userId: String, type: String): List<UserAction> {
        val response = client.get("https://dbcopy-backend.vercel.app/$type/$userId")
        val data: List<UserAction> = response.body()
        print("fetchUserAction Response: ${response.status}, ${response.bodyAsText()}")
        return data
    }
    //get users's trips
    suspend fun getUserTrips(userId: String): List<Trip> {
        val response = client.get("https://dbcopy-backend.vercel.app/trips/$userId")
        println("Response From Api Trips: ${response.bodyAsText()}")
        val data: List<Trip> = response.body()
        return data
    }

    //edit or create a trip
    suspend fun tripUpdateOrCreate(trip: Trip) {
        val response = client.post("https://dbcopy-backend.vercel.app/trips") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(trip))
        }

        println("Response: ${response.status}")

    }

    suspend fun deleteTrip(tripId: String) {
        val response = client.get("https://dbcopy-backend.vercel.app/deletetrip/$tripId")
        println("Delete Response: ${response.status}, ${response.bodyAsText()}")
    }

    //get user by email address
    suspend fun getUserByEmailAddress(email: String): User? {
        val encodedEmail = email.encodeURLPath()
        val response = client.get("https://dbcopy-backend.vercel.app/users/$encodedEmail")

        val raw = response.bodyAsText()
        println("Response From API: $raw")

        // decode list
        val users: List<User> = response.body()
        println("USERS LIST, ${users.firstOrNull()}")
        return users.firstOrNull()
    }

    //getImageUrl is "kotlinized" from function in Mapnook-mono
    fun getImageUrl(id: String, width: Int? = null, height: Int? = null): String {
        if (width == null && height == null) {
            println("Warning: getImageUrl - retrieving full-sized image as width and height are both undefined.")
        }
        // Use Ktor's URLBuilder for a safe and idiomatic way to build URLs
        val url = URLBuilder("https://img.mapnook.com/").apply {
            encodedPath = "filters:strip_exif()/$id"
            width?.let { parameters.append("width", it.toString()) }
            height?.let { parameters.append("height", it.toString()) }
        }.buildString()

        return url
    }
}
