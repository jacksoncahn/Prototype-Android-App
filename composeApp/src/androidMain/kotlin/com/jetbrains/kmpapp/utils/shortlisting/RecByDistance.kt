package com.jetbrains.kmpapp.utils.shortlisting

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.maps.android.SphericalUtil
import com.google.android.gms.maps.model.LatLng
import com.mapnook.api.MyPostsViewModel
import com.mapnook.api.Post
import java.util.PriorityQueue
import kotlin.collections.emptyList

//val p1 = LatLng(1.0, 1.0)
//val p2 = LatLng(2.0, 2.0)

//right now, function reccomends 5 (or less, if we don't have 5) closest activites
//in the future we might want to reccomend all activities inside a certain distance threshold
fun RecommendByDistance(base: Post, activities: List<Post>, trip: MyPostsViewModel.Trip): List<Post> {
    val newActivities = mutableListOf<Post>()
    for (post in activities) {
        if (!trip.posts.contains(post)) {
            newActivities.add(post)
        }
    }
    if (newActivities.size <= 5) {
        return newActivities
    }
    // Max-heap sorted by *farthest* distance first
    val heap = PriorityQueue<Pair<Post, Double>>(compareByDescending { it.second })

    for (post in newActivities) {
        val dist = SphericalUtil.computeDistanceBetween(
            LatLng(base.location[0], base.location[1]),
            LatLng(post.location[0], post.location[1])
        )

        if (heap.size < 5) {
            // Still filling the heap
            heap.add(post to dist)
        } else if (dist < heap.peek().second) {
            // Replace the farthest item with this closer one
            heap.poll()
            heap.add(post to dist)
        }
    }

    // Return the 5 closest, sorted from nearest → farthest in list form
    return (heap.sortedBy { it.second }.map { it.first }).toList()
}

