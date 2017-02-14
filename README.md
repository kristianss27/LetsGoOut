# Lets Go out
# CodePath Final Project

**Lets Go Out** is an android app that allows you to plan out any activity like hang out, go out, have dinner etc. in advance and keep informing your friends with it. The app use "Facebook Login" to allow the user signs into the app and uses several features this social media has in order to connect friends who are using "Let's go out" also. Once the user sign in, the app shows a list of the upcoming plans (In case any has been configured) and will have a floating button to add a "New plan".

The app provides a simple questionary to give a description of what the user is planning, when (the date) and where (writing a postal code) to then show a search screen that utilizes "Yelp" in other to show and check places related to the user interest. Once the user has choosen at least one possibly place, the app will show a preview of the plan with all the previous info and decide add or no the plan to the main list. At the end, the app will automatically notificate to the friends and they can join the plan.

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign into the app** using facebook login.
* [x]	User can **add a plan** pressing a button and the app will launch a questionary with three basic forms that the user has to answer.
  * [x] Form 1 - What are you planing?: Through radio buttons, the user will select one of the options and give a little description about it (Limited by a certain number of characters).
  * [x] Form 2 - When?: Using a Date Picker, the user can setup the date.
  * [x] Form 3 - Where?: Write a postal code to define the city.
* [x] User can **search any type of business related to any interest** by specifying a query and launching a search. Search displays a result list utilizing Yelp.api.
  * [x] User can **scroll down to see more results**. The maximum number of results is limited by the Yelp API.
  * [x]	The result list diplays the image,name,location,rating,reviews and phone of the entity from Yelp.
  * [x] User can tap on any entity listed to view the contents in an embedded browser.
  * [x] User can **add or remove any result ** by dragging to the right to "add" or to the left to "eliminate" the item    displayed on the list.
  * [x] User can **see a preview** of the plan,hitting a floating button ubicated to the right bottom of the search screen.
* [x] User can **look the preview and modify it** on the preview screen.
  * [x]	Split the preview screen with a description on the top and a tab panel below that shows the list of possible business entities where the plan would happen.
  * [x] The user can **setup the time of every entity** within the list, across tap a clock icon.
  * [x] The user can **add or remove any result ** by dragging to the right to "add" or to the left to "eliminate" the item    displayed on the list.
  * [x] The user can **discard** any item on the list, dragging the item to the left
  * [x] The user can **confirm and add the plan** by tapping a floating button ubicated on the preview screen
  	* [x] Once confirmed the plan, the app will inform about the plan to the user's facebook friends via a notification. 		Limitation: Users friend should be using "Let's go out app"
* [x] User have **the plan list screen** populated by all the plans the user has added.
  * [x]	User can **check the information** by hitting any plan listed and go to the final description screen.
* [x] User can **edit the information** using the editable final description screen. 
* [x] The app **sends notification** to user's friends once the user add any plan to his/her plans list. 

## Video Walkthrough

[Here's a walkthrough of implemented user stories](https://www.youtube.com/watch?v=2FQRCWqnOgM&t=77s)

## Notes
This is a Solo Project developed by [Cristian Sanchez](https://www.linkedin.com/in/kristianss27)
Using [Heroku Platform](https://www.heroku.com/) and the following libraries

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [Parse](http://parseplatform.github.io/docs/android/guide/)
- [Facebook Sdk](https://developers.facebook.com/docs/android/)
- [Butterknife](jakewharton.github.io/butterknife/)
- [Gson](https://github.com/google/gson)
- [Yelp Android](https://github.com/Yelp/yelp-android)
- [Parceler](https://guides.codepath.com/android/Using-Parceler)

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
