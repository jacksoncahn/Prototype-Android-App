package com.mapnook.api.trips

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
//import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonElement

@Serializable
data class Housing(
    var address: String? = null,
    var location: List<Double>? = null
)
@Serializable
data class TripActivity (
    val activityId: String? = null,
    val day: Int? = null
)


@Serializable
data class Trip(
    val id: String? = null,
    val primaryUser: String? = null,
//    val startDay: LocalDateTime? = null,
//    val endDay: LocalDateTime? = null,
    val users: JsonElement? = null,
    val housing: JsonElement? = null,
    val numDays: Int? = null,
    val tripActivities: JsonElement? = null,
    val name: String? = null,
    @Transient var housingReadable: Housing = Housing(null, null),
    @Transient var tripActivitiesReadable: MutableList<TripActivity> = emptyList<TripActivity>().toMutableList()
)
