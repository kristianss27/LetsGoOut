# Lets Go out
# CodePath Project

**Lets Go Out app** is an android app that allow to the user find a bunch of information using yelp to ask about some services the businesses on Yelp offer nowaday. Normally a user wants to do some activities like dancing or even organizing a part. Through the app they dont need to open Yelp App and checks all the places that are favourable for him/her to pursue their interest and contacts every place and its timings. With the "Wanttoknow" app, the user can select the list of the places, from the overall list and send a message to all of the selected places at once, stating that the user wants to learn dancing or the user wants to organize a birthday party.
This app uses the Yelp API to retrieve information on the Ratings and Location with the Phone Number and the Name of the Place. 

Time spent: **** hours spent in total

App Flow - 
User Sign up / Login in the App as a Person or Businesses, this second registered on Yelp as required
User 1: Any Person that would use the app to contact or get a service from any a business registered on Yelp. This user will use the email as “User Name” and define a password to Sign up on our App.Then, They can Log in on the app with the same information registered
User 2: All businesses being registered on Yelp


Flow for the User 1:
Once the user 1 was registered on our app the app will show the Search Screen
On the search screen the user 1 can search anything using the Search Bar
After writing on the Search Bar, the user can get a result list with all the businesses related to the searching
The user can select any business with the button “Add” and this button will add the business to a New list called Personal List.
The user can hit the button “Next” to Jump to the Personal List
Once on the Personal List, the user can verify if the first selection of businesses was correct. In case it wasn’t right, the user can swipe to the left to delete the service chosen before
If the user is sure of all the selections, the user can hit the button “with a envelop” to send a message and the app will show an screen with all the options to write message by a notification and create a door to open a Chat 
Important note: We are assuming for the first release that both users are using the app. 
The user will wait until getting an answer from every business contacted.
After the business answered.

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign in to the app** using OAuth login, via Email.
* [x]	User can **view thge list of places from their based on the search criteria.
  * [x] User is displayed the username, name, and body for each recent activity.
  * [x] User can view more media as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of images is unlimited.
* [x] User can **compose and upload a new media**
  * [x] User can click a “Compose” icon to create a Message.
  * [x] User can click a “Search” icon to search for the places, and populate the results in the List View.
	* [x] The user will have a list of services selected before and send a message
	* [x] The message will be generated with some general question. What would you like to know
	When you probably want to do this and how to contact them
* [x] When the user send the message, automatically the app will send a notiication to that business registered in the app
* [x] The app provides a way to create that connection between customer and businesses through a list of request that when you can hit the item, you could open a chat with every service contacted
## Video Walkthrough

Here's a walkthrough of implemented user stories: http://i.imgur.com/KjpkuC6.gif

<[Video Walkthrough} />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2016] [Cristian Sanchez]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
