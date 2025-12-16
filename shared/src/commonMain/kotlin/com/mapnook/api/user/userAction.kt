package com.mapnook.api.user

import kotlinx.serialization.Serializable
//import kotlinx.datetime.LocalDateTime

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
