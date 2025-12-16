package com.jetbrains.kmpapp.utils.shortlisting

import com.google.maps.android.SphericalUtil
import com.google.android.gms.maps.model.LatLng
import com.mapnook.api.activities.Activity
import com.mapnook.api.trips.Trip
import java.util.PriorityQueue

//val p1 = LatLng(1.0, 1.0)
//val p2 = LatLng(2.0, 2.0)

//right now, function reccomends 5 (or less, if we don't have 5) closest activites
//in the future we might want to reccomend all activities inside a certain distance threshold
fun RecommendByDistance(base: List<Double>, activities: List<Activity>, trip: Trip): List<Activity> {
    println("RECOMMEND FUNC called")
    println("RECOMMEND FUNC activities: $activities")
    val newActivities = mutableListOf<Activity>()
    for (activity in activities) {
        if (!trip.tripActivitiesReadable.any { it.activityId == activity.id }) {
            newActivities.add(activity)
        }
    }
    if (newActivities.size <= 5) {
        println("RECOMMEND FUNC recommended activities (not heap): $newActivities")
        return newActivities
    }
    // Max-heap sorted by *farthest* distance first
    val heap = PriorityQueue<Pair<Activity, Double>>(compareByDescending { it.second })

    for (activity in newActivities) {
        val dist = SphericalUtil.computeDistanceBetween(
            LatLng(base[0], base[1]),
            LatLng(activity.location[0], activity.location[1])
        )

        if (heap.size < 5) {
            // Still filling the heap
            heap.add(activity to dist)
        } else if (dist < heap.peek().second) {
            // Replace the farthest item with this closer one
            heap.poll()
            heap.add(activity to dist)
        }
    }

    // Return the 5 closest, sorted from nearest → farthest in list form
    println("RECOMMEND FUNC recommended activities heap: $heap")
    return (heap.sortedBy { it.second }.map { it.first }).toList()
}

