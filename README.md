# Kotlin Multiplatform app template

This is a basic Kotlin Multiplatform app template for Android and iOS. It includes shared business logic and data handling, and native UI implementations using Jetpack Compose and SwiftUI.

# Codebase Overview

The major folders added to this project are in composeApp/src/androidMain/kotlin/com/jebtrains/kmpapp
* components, contains ActivityCard, ListCard, MapsSearchBar, Menu, Searchbar
* screens, contains subdirectories auth, basic, and trip
* theme (not used currently), can be edited to implement the light and dark themes for the app
* utils, contains Bitmap, and shortlisting directory
* auth, contains SignInScreen
* basic, contains all basic screens, such as Home, or Settings
* trip, contains all trip-related screens, including Trip, TripList, and TripPlanner

In shared/src/commonMain/kotlin/com/mapnook, we have our backend
* api subdirectory includes an ApiClient object, to fetch from our database, users, activities, etc. As well as a posts subdirectory, which itself contains Post file (dataclass), and MyPostsViewModel file (viewmodel) - used to store fetched posts and handle user actions clientside (for now)
* auth, contains User file (dataclass for user, and myUserViewModel to store fetched user from db) - used to store fetched user