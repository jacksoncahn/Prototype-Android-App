package com.mapnook.api

import com.mapnook.api.posts.Activity
import com.mapnook.auth.User
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getPosts(): List<Activity> {
        //production url
        val response = client.get("https://dbcopy-backend.vercel.app/activities")
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

    suspend fun getUserByEmailAddress(email: String): User? {
        val response = client.get("https://dbcopy-backend.vercel.app/users/$email")

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
