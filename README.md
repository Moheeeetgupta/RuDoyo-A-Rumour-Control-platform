# RuDoYo- A rumour control Android App


<img src="https://user-images.githubusercontent.com/57634381/128712843-b4f252b6-f6f8-4208-adf8-d0946e6966db.png" align="left"
width="200" hspace="10" vspace="10">

Rudoyo is an innovative android app powered by Machine Learning capabilities following the latest material design guidelines to solve one of the most pronounced problems of today's world,the rumours spread by images and videos and it represents a great combination of Team Work and coordination.

RuDoYo is available on the Google Play Store.

<p align="left">
<a href="https://play.google.com/store/apps/details?id=com.rumooursindoyo.moheeeetgupta&hl=en&gl=US">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
       </a>  
       </p>
       
## About

It is a rumour control platform which helps users to verify rumours and clarify their perception about any news. To achieve this, the app provides two ways, first one is that the user can post any rumour which he/she wants to clarify, on Rudoyo post section, this post section is visible to each and every registered Rudoyo user. The post has an option of voting and reviews system through which all registered users can vote either in favour or against the rumour. And the second one is that we have a section of ‘video verification’ in our application, as Youtube videos also spread many rumours and mislead people. For video clarification, users need to share the youtube link in our app section and they will get the truth and false percentage of the particular video. 
For Video verification, we have fetched top 500 comments on the video from the you tube api  and performed sentiment analysis by the help of a tflite model created by tensorflow library,and trained with IMDB movie reviews dataset which has 25000 labeled(positive/negative) movie reviews.


## Features

The android app lets you:
- Login,Logout
- Recover password
- Register with email verification
- Profile setup
- Post Rumours and get announcement of post uploaded
- Crop and compress image before posting
- upvote,downvote or be neutral on any rumour post.
- Review any rumour
- Share any youtube video link to the app and predict its truth probabilty by the help of ML model

## Technologies, Libraries and Components Used
Technologies and Libraries used:
- Java  
- XML
- Firebase firestore database
- Firebase storage to store heavy data
- Firebase Authentication
- Glide library for image loading
- TensorflowLite Task api library
- Retrofit Library
- Youtube Api V3
- Material design guidelines
- Arthurhub’s android image cropper library
- Hedodenhof’s Circle image view library
- Zetbaitsu’s image compression library
- Airbnb’s Lottie animations 
- Media player api

Android Components Used:
- RecyclerView
- LinearLayout
- Fragments
- ConstraintLayout
- RelativeLayout
- CardView
- CircleImageView
- Material text fields ( TextInputEditText )
- Explicit and implicit intents
- Intent filters 
- ProgressBar
- ToolBar
- Menu Bar 
- BottomNavigationView
- FrameLayout
- FloatingActionButton
- CircularImageView
- TextView, Button, EditText, ImageView
- ScrollView.


## Screenshots

[<img src="/readme/Wallabag%20Reading%20List.png" align="left"
width="200"
    hspace="10" vspace="10">](/readme/Wallabag%20Reading%20List.png)
[<img src="/readme/Wallabag%20Article%20View.png" align="center"
width="200"
    hspace="10" vspace="10">](/readme/Wallabag%20Article%20View.png)

