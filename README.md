# Lets Go out
# CodePath Project

**Lets Go Out** is an android app that allows you to plan out any activity like hang out, go out, have dinner etc. in advance and keep informing your friends with it. The app use "Facebook Login" to allow the user signs into the app and uses several features this social media has in order to connect friends who are using "Let's go out" also. Once the user sign in, the app shows a list of the upcoming plans (In case any has been configured) and will have a floating button to add a "New plan".

The app provides a simple questionary to give a description of what the user is planning, when (the date) and where (writing a postal code) to then show a search screen that utilizes "Yelp" in other to show and check places related to the user interest. Once the user has choosen at least one possibly place, the app will show a preview of the plan with all the previous info and decide add or no the plan to the main list. At the end, the app will automatically notificate to the friends and they can join the plan.

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign into the app** using facebook login.
* [x]	User can **add a plan** pushing a button and the app will launch a questionary with three basic forms that the user has to answer. Forms What are you planing? When(Date)? and Where(Postal code that define the city)
  * [x] Form 1 - What are you planing?: Through radio buttons, the user will select one of the options and give a little description about it (Limited by a certain number of characters).
  * [x] Form 2 - When?: Using a Date Picker, the user can setup the date.
  * [x] Form 3 - Where?: Write a postal code to define the city.
* [x]	The user can **search any type of business related to any interest ** by specifying a query and launching a search. Search displays a result list utilizing Yelp.api and It is endless scrollable.
  * [x]	The result list diplays
  
  The user can **add or remove any result ** by dragging to the right to add or to the left to eliminate the item    displayed on the list.
  * [x]	The user can **add or remove any result ** by dragging to the right to add or to the left to eliminate the item    displayed on the list.
* [x] User can view more media as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of images is unlimited.
* [x]	User can **sign into the app** using facebook login.
* [x]	User can **see the list of upcoming plans**.
* [x]	User can **add a plan** using .
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

Here's a walkthrough of implemented user stories: https://www.youtube.com/watch?v=2FQRCWqnOgM&t=77s

<[Video Walkthrough} />

## Notes



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
