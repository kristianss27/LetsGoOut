# Want to Know
# Project 3 - *Want To Know*

**Want To Know app** is an android app. For example: If a user is wants to learn dancing or singing or organize a party, the user, generally opens the Yelp App and checks all the places that are favourable for him/her to pursue their interest and contacts every place and its timings. With the "Wanttoknow" app, the user can select the list of the places, from the overall list and send a message to all of the selected places at once, stating that the user wants to learn dancing or the user wants to organize a birthday party.
This app uses the Yelp API to retrieve information on the Ratings and Location with the Phone Number and the Name of the Place. 

Time spent: **** hours spent in total

App Flow - 
User Sign up / Login in the App as a Person or Businesses, this second registered on Yelp as required
User 1: Any Person that would use the app to contact or get a service from any a business registered on Yelp. This user will use the email as “User Name” and define a password to Sign up on our App.Then, They can Log in on the app with the same information registered
User 2: All businesses being registered on Yelp
	For this second user the app will provide Yelp Login as mandatory


Flow for the User 1:
Once the user 1 was registered on our app the app will show the Search Screen
On the search screen the user 1 can search anything using the Search Bar
After writing on the Search Bar, the user can get a result list with all the businesses related to the searching
The user can select any business with the button “Add” and this button will add the business to a New list called Personal List.
The user can hit the button “Check” to Jump to the Personal List
Once on the Personal List, the user can verify if the first selection of businesses was correct. In case it wasn’t right, the user can hit the button “Delete” to get rid of any of them
If the user is sure of all the selections, the user can hit the button “Contact them” and the app will show an screen with all the options to write a general or an individual message by a notification. 
Important note: We are assuming for the first release that both users are using the app. 
The user will wait until getting an answer from every business contacted.
After the business answered, They user will get a notification that will open a new screen to check the answers

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign in to the app** using OAuth login, via Email.
* [x]	User can **view thge list of places from their based on the search criteria.
  * [x] User is displayed the username, name, and body for each recent activity.
  * [x] User can view more media as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of images is unlimited.
* [x] User can **compose and upload a new media**
  * [x] User can click a “Compose” icon to create a Message.
  * [x] User can click a “Sort” icon to sort the results in the ascending order.
  * [x] User can click a “Search” icon to search for the places, and populate the results in the List View.

The following **optional** features are implemented:

* [] User can **click a select the Place within the app ** and will be redirected to the details view. The click on the Reviews will launch the web browser with relevant page opened.
* [] User can **pull down to refresh media timeline**
* [] Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from, it is also saved for use in offline mode.
* [x] User can tap a media to **open a detailed media view in browser**
* [] User can **select share from detail view to respond to a media**

The following **bonus** features are implemented:

* [x] User can see embedded image media within the media detail view
* [] User can watch embedded video within the media detail view
* [] Compose a media  functionality is build using modal overlay
* [] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] [Leverage RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) as a replacement for the ListView and ArrayAdapter for all lists of tweets.
* [] Move the "Compose" action to a [FloatingActionButton](https://github.com/codepath/android_guides/wiki/Floating-Action-Buttons) instead of on the AppBar.
* [ ] On the Home timeline, leverage the [CoordinatorLayout](http://guides.codepath.com/android/Handling-Scrolls-with-CoordinatorLayout#responding-to-scroll-events) to apply scrolling behavior that [hides / shows the toolbar](http://guides.codepath.com/android/Using-the-App-ToolBar#reacting-to-scroll).
* [] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [ ] Enable your app to [receive implicit intents](http://guides.codepath.com/android/Using-Intents-to-Create-Flows#receiving-implicit-intents) from other apps.  When a link is shared from a web browser, it should pre-fill the text and title of the web page when composing a tweet.

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<[Video Walkthrough} />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2016] [Swati Singh and Cristian Sanchez]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
