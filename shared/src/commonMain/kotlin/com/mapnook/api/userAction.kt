package com.mapnook.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
//import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonElement

//@Serializable
//data class Housing(
//    var address: String? = null,
//    var location: List<Double>? = null
//)
//@Serializable
//data class TripActivity (
//    val activityId: String? = null,
//    val day: Int? = null
//)


@Serializable
data class UserAction(
    val id: Int? = null,
    val activityId: String? = null,
    val userId: String? = null,
    val type: String? = null,
)
