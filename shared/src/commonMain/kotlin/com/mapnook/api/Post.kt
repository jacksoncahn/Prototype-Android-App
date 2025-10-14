package com.mapnook.api


import kotlinx.serialization.Serializable
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
    val properties: String?,
    val nextShowTime: String?,
    val importance: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val deletedAt: String?
)
