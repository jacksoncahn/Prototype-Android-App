# Kotlin Multiplatform app template

This app was built off of a basic Kotlin Multiplatform app template for Android and iOS. It includes shared business logic and data handling, and native UI implementations using Jetpack Compose and SwiftUI.
* Swift UI is not implemented as of yet- it remains exactly like the template- but the Android frontend has been redone. 

# Codebase Overview

Android Only - ./composeApp/src/androidMain/kotlin/com/jetbrains/kmpapp
* ./App.kt - the main composable function that contains routes to all pages, the menu button, and some code to be executed right after the app signs in.
* ./MainActivity - the entrypoint for the app.
* ./MapnookInit - runs global app initialization when the app process starts
* ./components
  * ActivityCard.kt, contains a composable to display an activity card in the Home.kt composable; has buttons which write to the database when user clicks them.
  * ListCard.kt, contains a composable to display a list card for an activity on the MyLists.kt composable and Trip.kt composable.
  * MapsSearchBar.kt, contains a composable to display a search bar that directly accesses the Places API to provide an address and lng,lat for the home base in Trip.kt.
  * Menu.kt, contains a composable that has a show/hide menu using a button, and each button inside the menu routes to a specific page of the app.
  * Searchbar.kt, contains a searchbar that does nothing, just for visuals, used in Home.kt.
  * TripListCard.k, contains a list card like the ListCard.kt but with an optional activity input, which accounts for when a trip does not have any activities to display the picture of.
* ./screens/auth
  * SignInScreen.kt, screen displayed when user has not "signed in" with their email. Calls method inside UserViewModel to fetch user data.
    * IMPORTANT NOTE: This app does not have a real authentication system, and can only be used by if the user has their email address in the tb1 database (the copy Mapnook database).
* ./screens/basic
  * About.kt - basic about page, almost entirely unimplemented
  * AddPlaceOrEvent.kt - entirely unimplemented.
  * Contact.kt - basic contact page, almost entirely unimplemented.
  * FAQ.kt - basic FAQ page, almost entirely unimplemented.
  * Help.kt - basic help page, almost entirely unimplemented.
  * Home.kt - main page of the app, uses the Maps API to display a map underneath all other components, also has basic map interaction buttons, half implemented, shows the selected activity as an ActivityCard, and shows each marker of activity fetched from the database.
  * MyLists.kt - user's  want to go, not for me, skipped, and visited activity lists, displayed as ListCards.
  * Settings.kt - basic settings page, almost entirely unimplemented.
* ./screens/trip
  * PlanTrip.kt - displays a user's want to go activities, and allows them to be selected, whereupon the user is taken to a new page where the fill out some information about their trip (just the trip name at the moment).
  * TripPlanner.kt - displays a page of a new trip-in-the-making, allows user to type in a trip name, whereupon the user can create their trip- it gets saved in the database.
  * Trip.kt - displays a trip w/ three tabs- trip activities, recommended activities, and trip details, the last tab displays the MapsSearchBar which lets a user search for, and set, their homebase to allow recommendations.
  * TripList - displays a list of the user's trips using the TripListCard

There are also some basic resource files in composeApp/src/main/res & composeApp/androidMain/res

In ./shared/src/commonMain/kotlin/com/mapnook, we have our backend - meant to be used on both platforms:
* The api folder within contains an ApiClient object, to interact with our database- users, activities & trips. As well as three subdirectories:
* api/user contains a file userAction.kt which describes the dataclass userAction, used to fetch and store user's actions w.r.t. activities- marking an activity "want to go", "not for me" etc.
* api/user also contains UserViewModel.kt- the main file that interacts with the ApiClient and stores fetched user data.
  * class UserViewModel:
    * attributes:
      * user: User?
      * trips: List<Trip>
      * tempTripActivities: List<Activity>
      * tempUserStorage: User?
      * wanttogo: List<String>
      * visited: List<String>
      * notforme: List<String>
      * skipped: List<String>
      * wanttogoActivities: List<Activity>
      * visitedActivities: List<Activity>
      * notformeActivities: List<Activity>
      * skippedActivities: List<Activity>
    * methods:
      * fetchUser() - takes an email as input and fetches user associated with that email (and their data).
      * createOrUpdateTrip() - takes a trip id, the user's id, along with optional values that can be used to create or update a trip.
      * updateTripActivities() - takes a trip id, the user's id, an activity, and a boolean indicating whether to remove the activity from the trip or add it to the trip - used for updating the trip's activities with new activities from the recommended list, or removing activities from trip.
      * fetchUserTrips() - fetches all trips associated with the user.
      * deleteTrip() - deletes a trip.
      * saveUserAction() - saves a user action (i.e. a user marking an activity as "want to go", "not for me" etc.)
      * fetchUserActions() - fetches all user actions associated with the user.
      * deleteUserAction() - deletes a user action.
* api/activities contains an ActivitiesViewModel, used for fetching new activities from database and storing them clientside.
* api/activities also contains an Activity data class, as well as a file named extras.kt, which implements a simple function to fetch activity by id.
* api/trips contains a data class for a Trip.

