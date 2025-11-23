package com.jetbrains.kmpapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.tasks.await

@Composable
fun MapsSearchBar(
    onLocationSelected: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
//
//    // Ensure Places SDK is initialized
//    remember {
//        if (!Places.isInitialized()) {
//            Places.initialize(context, BuildConfig.MAPS_API_KEY)
//        }
//    }

    val placesClient = remember { Places.createClient(context) }

    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val token = remember { AutocompleteSessionToken.newInstance() }

    // Fetch autocomplete suggestions whenever query changes
    LaunchedEffect(query) {
        if (query.isBlank()) {
            predictions = emptyList()
            return@LaunchedEffect
        }

        try {
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()
            predictions = response.autocompletePredictions.take(5)
        } catch (e: Exception) {
            e.printStackTrace()
            predictions = emptyList()
        }
    }

    Column(modifier = modifier) {

        // Search input
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            query = ""
                            predictions = emptyList()
                        }
                    )
                }
            },
            placeholder = { Text("Search for a location") }
        )

        // Prediction list
        predictions.forEach { prediction ->
            Text(
                text = prediction.getFullText(null).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Fetch place details to get LatLng
                        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
                        val request = FetchPlaceRequest.builder(
                            prediction.placeId,
                            placeFields
                        ).build()

                        placesClient.fetchPlace(request)
                            .addOnSuccessListener { result ->
                                result.place.let { place ->
                                    onLocationSelected(place)
                                }
                                query = prediction.getPrimaryText(null).toString()
                                predictions = emptyList()
                            }
                            .addOnFailureListener { error ->
                                error.printStackTrace()
                            }
                    }
                    .padding(16.dp)
            )
        }
    }
}
