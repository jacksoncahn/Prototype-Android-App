package com.mapnook.api.posts

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement


@Serializable
data class Post(
    val id: String?,
    val slug: String?,
    val location: List<Double>,
    val status: String?,
    val name: String?,
    val tagline: String?,
    val summary: String?,
    val description: String?,
    val nativeName: String?,
    val nativeLanguageCode: String?,
    val primaryImageId: String?,
    val tags: List<String>?,
    val osm: JsonElement?,
    val properties: JsonElement?,
    val nextShowTime: String?,
    val importance: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val deletedAt: String?,

    //imageUrl is a variable because it is re-assigned in getPosts()
    //imageUrl is also Transient so the Post class can fully match w/ JSON
    //must set it to null in the constructor, otherwise it will throw an error
    @Transient var imageUrl: String? = null,
    @Transient var liked: Boolean = false,
    @Transient var visited: Boolean = false,
    @Transient var disliked: Boolean = false,
    @Transient var skipped: Boolean = false,

    //for rec by distance in trip planner
    @Transient var latlng: Any? = null
)
