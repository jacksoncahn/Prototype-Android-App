package com.mapnook.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapnook.api.ApiClient
import com.mapnook.api.posts.Activity
import com.mapnook.api.posts.Housing
import com.mapnook.api.posts.Trip
import com.mapnook.api.posts.TripActivity
import com.mapnook.api.posts.fetchActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.put
import kotlin.collections.plus

//private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class User (
    val id: String?,
    val displayName: String?,
    val bio: String?,
//    val tags: List<JsonObject>?,
    val avatarUrl: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
)

class UserViewModel: ViewModel() {
    var user by mutableStateOf<User?>(null)

//    var internalActivityViewModel = ActivitiesViewModel()

    var trips by mutableStateOf<List<Trip>>(emptyList())
    var tripsActivities by mutableStateOf<Map<String, List<Activity>>>(emptyMap())

    var tempUserStorage by mutableStateOf<User?>(null)
    
    var wanttogo by mutableStateOf<List<String>>(emptyList())
    var visited by mutableStateOf<List<String>>(emptyList())
    var notforme by mutableStateOf<List<String>>(emptyList())
    var skipped by mutableStateOf<List<String>>(emptyList())


    var wanttogoActivities by mutableStateOf<List<Activity>>(emptyList())
    var visitedActivities by mutableStateOf<List<Activity>>(emptyList())
    var notformeActivities by mutableStateOf<List<Activity>>(emptyList())
    var skippedActivities by mutableStateOf<List<Activity>>(emptyList())



    fun fetchUser(email: String) {
        viewModelScope.launch {
            try {
                val fetchedUser = ApiClient.getUserByEmailAddress(email)
                tempUserStorage = fetchedUser
                println("tempUserStorage, $tempUserStorage")
            } catch (e: Exception) {
                println("Error fetching user: ${e.message}")
            }
        }
    }

    suspend fun createOrUpdateTrip(
        id: String? = null,
        primaryUser: String?,
        name: String? = null,
        users: List<String>? = null,
        tripActivities: List<TripActivity>? = null,
        numDays: Int? = null,
        housing: Housing? = null
    ) {
        val usersJson: JsonArray? = users?.let { userList ->
            buildJsonArray {
                userList.forEach { userId ->
                    add(userId)
                }
            }
        }

        val tripActivitiesJson: JsonArray? = tripActivities?.let { tripActivities ->
            buildJsonArray {
                tripActivities.forEach { tripActivity ->
                    println("trip_Activity $tripActivity")
                    add(
                        buildJsonObject {
                            put("activityId", tripActivity.activityId)
                            put("day", tripActivity.day)
                        }
                    )
                }
            }
        }
        println("createOrUpdateTrip tripActivities: $tripActivities")

        val housingJson: JsonObject? = housing?.let { housing ->
            buildJsonObject {
                put("address", housing.address)
                housing.location?.let {
                    put("location", buildJsonArray {
                        it.forEach { value -> add(value) }
                    })
                }
            }
        }
        println("createOrUpdateTrip housing: $housingJson")


        if (id != null) {
            val trip = Trip(
                id = id,
                primaryUser = primaryUser,
                name = name,
                numDays = numDays,
                users = usersJson,
                housing = housingJson,
                tripActivities = tripActivitiesJson
            )
            ApiClient.tripUpdateOrCreate(trip)

        } else {
            val trip = Trip(
                primaryUser = primaryUser,
                name = name,
                numDays = numDays,
                users = usersJson,
                housing = housingJson,
                tripActivities = tripActivitiesJson
            )
            ApiClient.tripUpdateOrCreate(trip)
        }

        fetchUserTrips()
    }

    fun fetchUserTrips() {
        viewModelScope.launch {
            try {
                val fetchedTrips = ApiClient.getUserTrips(user!!.id!!)
                println("fetchedTrips called from userviewModel")

                println(fetchedTrips)

                //populate the TRANSIENT housingReadable & activities props for each trip
                for (trip in fetchedTrips) {
                    println("populating readablehousing & readableactivities")
                    try {
                        println("TRIP HOUSING ${trip.housing}")
                        println("TRIP ACTIVITIES ${trip.tripActivities}")

                        if (trip.housing != null && trip.housing !is JsonObject) {
                            println("Error: trip.housing is not a JsonObject")
                            continue // Skip to the next trip
                        }

                        trip.housing?.jsonObject?.let { housingObj ->
                            val locationArray = housingObj["location"]?.jsonArray
                            println("TRIP FETCH locationArray: $locationArray")
                            val address = housingObj["address"]?.toString()?.trim('"')
                            println("TRIP FETCH address: $address")

                            try {
                                if (address != null) {
                                    trip.housingReadable.address = address
                                }
                                if (locationArray != null) {
                                    trip.housingReadable.location = locationArray.map { it.toString().toDouble() }
                                }
                            } catch (e: Exception) {
                                println("Error setting housingReadable properties: ${e.message}")
                            }
                        }

                        if (trip.tripActivities != null && trip.tripActivities !is JsonArray) {
                            println("Error: trip.tripActivities is not a JsonArray")
                            continue // Skip to the next trip
                        }

                        val activitiesJsonArray = trip.tripActivities?.jsonArray
                        val activitiesList = mutableListOf<TripActivity>()

                        activitiesJsonArray?.let {
                            for (activity in it) {
                                println("activity iteration for trip: ${trip.name}")
                                try {
                                    activitiesList += TripActivity(
                                        activityId = activity.jsonObject["activityId"]?.toString()?.trim('"'), // check if activity.jsonObject["day"] is not null and is a number
                                        day = if (activity.jsonObject["day"] != null && activity.jsonObject["day"].toString()
                                                .trim('"').toIntOrNull() != null
                                        ) activity.jsonObject["day"]?.toString()?.trim('"')?.toInt() else null
                                    )
                                } catch (e: Exception) {
                                    println("Error creating TripActivity object: ${e.message}")
                                }
                            }
                        }
                        trip.tripActivitiesReadable = activitiesList
                        println("trip.tripActivitiesReadable: ${trip.tripActivitiesReadable}")

//                        val activityIds = trip.tripActivitiesReadable.map { it.activityId!! }

//                        fetchTripActivitiesDetails(activityIds, trip.id!!)



                    } catch (e: Exception) {
                        println("Error parsing housing JSON: ${e.message}")
                    }
                }

                trips = fetchedTrips
            } catch (e: Exception) {
                println("Error fetching user trips: ${e.message}")
            }
        }
    }

//    suspend fun fetchTripActivitiesDetails(activityIds: List<String>, tripId: String) {
//        for (activityId in activityIds) {
//            val activity = fetchActivity(activityId)
//            val activities = mutableListOf<Activity>()
//            if (activity != null) {
//                val activityDetails = fetchActivity(activityId)
//                if (activityDetails != null) {
//                    activities += activityDetails
//                }
//            }
//            tripsActivities += tripId to activities
//        }
//
//    }

    suspend fun deleteTrip(tripId: String) {
        trips -= trips.first { it.id == tripId }
        ApiClient.deleteTrip(tripId)
    }

    suspend fun saveUserAction(activityId: String, type: String) {
        ApiClient.saveUserAction(user!!.id!!, activityId, type)
        fetchUserActions(type)
    }
    
    suspend fun fetchUserActions(type: String) {
        val response = ApiClient.fetchUserAction(user!!.id!!,type)
        when (type) {
            "yes" -> {
                for (userAction in response) {
                    if (userAction.activityId !in wanttogo) {
                        val activity = fetchActivity(userAction.activityId!!)
                        wanttogo += userAction.activityId
                        if (activity != null) {
                            wanttogoActivities += activity
                        }
                    }
                }
            }
            "no" -> {
                for (userAction in response) {
                    if (userAction.activityId !in notforme) {
                        val activity = fetchActivity(userAction.activityId!!)
                        notforme += userAction.activityId
                        if (activity != null) {
                            notformeActivities += activity
                        }
                    }
                }
            }
            "skipped" -> {
                for (userAction in response) {
                    if (userAction.activityId !in skipped) {
                        val activity = fetchActivity(userAction.activityId!!)
                        skipped += userAction.activityId
                        if (activity != null) {
                            skippedActivities += activity
                        }
                    }
                }
            }
            "visited" -> {
                for (userAction in response) {
                    if (userAction.activityId !in visited) {
                        val activity = fetchActivity(userAction.activityId!!)
                        visited += userAction.activityId
                        if (activity != null) {
                            visitedActivities += activity
                        }
                    }
                }
            }
        }

    }

    suspend fun deleteUserAction(activityId: String, type: String) {
        ApiClient.deleteUserAction(user!!.id!!, activityId, type)
        wanttogo -= activityId
        wanttogoActivities -= wanttogoActivities.first { it.id == activityId }
        fetchUserActions(type)
    }

}

