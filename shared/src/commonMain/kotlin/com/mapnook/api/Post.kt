package com.mapnook.api


import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Post(
    val Id: String,
    val Slug: String,
    val location: Location,
    val status: Boolean,
    val name: String,
    val tagline: String,
    val summary: String,
    val description: String,
    val nativeName: String,
    val nativeLanguageCode: String,
    val primaryImageId: String,
    val tags: List<String>,
    val osm: String,
    val properties: String,
    val nextShowTime: String,
    val importance: Int,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String
)