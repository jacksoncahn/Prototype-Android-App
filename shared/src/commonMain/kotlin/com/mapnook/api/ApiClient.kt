package com.mapnook.api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getPosts(): List<Post> {
        val data: List<Post> = client.get("https://mapnook.com/api/activities").body()
        for (post in data) {
            println("post: ${post.name}, ${post.id}, ${post.tags}")
            if (post.primaryImageId != null) {
                post.imageUrl = getImageUrl(post.primaryImageId, 300, 300)
            }
        }
        return data
    }
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
