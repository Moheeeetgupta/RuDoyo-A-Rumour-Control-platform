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
       
## Description

It is a rumour control platform which helps users to verify rumours and clarify their perception about any news. To achieve this, the app provides two ways, first one is that the user can post any rumour which he/she wants to clarify, on Rudoyo post section, this post section is visible to each and every registered Rudoyo user. The post has an option of voting and reviews system through which all registered users can vote either in favour or against the rumour. And the second one is that we have a section of ‘video verification’ in our application, as Youtube videos also spread many rumours and mislead people. For video clarification, users need to share the youtube link in our app section and they will get the truth and false percentage of the particular video. 
For Video verification, we have fetched top 500 comments on the video from the you tube api  and performed sentiment analysis by the help of a tflite model created by tensorflow library,and trained with IMDB movie reviews dataset which has 25000 labeled(positive/negative) movie reviews.


## Features

The android app lets you:
- Sign In,Sign Out
- Recover password
- Register with email verification
- Profile setup
- Post Rumours and get announcement of post uploaded
- Crop and compress image before posting
- upvote,downvote or be neutral on any rumour post.
- Review any rumour
- Share any youtube video link to the app and predict its truth probabilty by the help of ML model

## Brief Process Flow 

- When the user taps on the launcher icon,then firstly he sees the splash screen merged with a lottie animation.
- After that if user is not logged in then login activity appears.
- If user is logged in then Main activity with home fragment appears,Main activity has three fragments home fragment ,verify video fragment ,account fragment which could be navigated by a bottom navigation view.
- If user is not logged in but if he is already registered then he will login by the help of firebase authentication service and go to the same Main activity .
- If the user has forgot password then he can recover it by getting an email to change password.
- If he is not registered then he can register by just entering his email and password and by tapping on the email sent to him for email verification.
- And then he could set up his profile and user name by the help of firebase storage to store the profile picture and firebase firestore to store the user details like profile pic link and user name.
- The user profile is loaded from the implcit intent to the app UI by using  cropper ,compressor and glide libraries.
- Now in the home fragment users can scroll the posts by the help of recyclerview having a custom adapter for posts view.
- The user can upvote ,down vote and be neutral with any post with the help of firebase firestore which contains all the data about every post.
- The users can also review the post by giving comments and also they can also see other's reviews.
- Users can tap on the floating action button to go to the new post activity where if user taps on the image placeholder,a implicit intent is thrown to the android OS and android shows us the various option apps to select image.
- After selecting, user can crop the image by a image cropper library and this cropped image is compressed by a image compression library and gets uploaded inside the firebase storage and its link with the post’s description, time stamp, user details and posts voting and review detail is stored inside the firebase firestore.
- And hence when the post is uploaded the app will announce, by the help of media player api ,that the post is uploaded,it will get verified soon,and now this post can be seen by any user.
- Now,In verify video fragment ,the user can paste any you tube link or can directly share the you tube link to our app by the help of intent filters,and if the link is not of youtube then this will also be detected by the app.
-  when user taps on the predict button ,the app will make an api call through the youtube V3 api and fetch the top 500 comments of that video by the help of retrofit library .
-  The api returns the data in json formate but retrofit helps us in parsing the json and then the text of the comment is passed to the Nlcalssifier class which classifies the comments text into two categories by the help of a ML model which was initially created by the help of tensorflow and trained by the IMDB movie reviews dataset which has 25000 labeled movie reviews and converted to the tflite version so that this model could be used by mobile devices.
-  After text classification the positive and negative sentiments of text comment is displayed on the UI as truth and false probability .
-  In Account fragment ,the users can update their profile details.


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

| Sign In | Sign Up | Home Fragment |
|:-:|:-:|:-:|
| ![First](https://user-images.githubusercontent.com/57634381/128724230-425601d6-27c2-4a1f-b936-fdaba33f8173.jpeg) | ![Sec](https://user-images.githubusercontent.com/57634381/128724541-04aef8e2-00ea-41ed-ba9a-8bb525bf2906.jpeg) | ![Third](https://user-images.githubusercontent.com/57634381/128724946-d671fed2-596d-49b7-8396-2b56eac2b6aa.jpeg)

| Verify Video Fragment | NewPost Activity | Implicit Intent |
|:-:|:-:|:-:|
|![Fourth](https://user-images.githubusercontent.com/57634381/128724981-4086986e-9dbb-40eb-9556-878db348305a.jpeg) | ![Fifth](https://user-images.githubusercontent.com/57634381/128725035-131fbd1c-ff72-4893-b633-73f48e7faddc.jpeg) | ![Sixth](https://user-images.githubusercontent.com/57634381/128725136-c12b7f68-bd60-4608-90c0-ccd42100d8b4.jpeg) |

| Image Cropper | Uploading... |
|:-:|:-:|
| ![Third](https://user-images.githubusercontent.com/57634381/128725110-bd81ac21-2814-4fdc-bb2e-c8c8103ac317.jpeg) | ![Fourth](https://user-images.githubusercontent.com/57634381/128725195-663a8777-5281-472c-b798-b992b5a14265.jpeg) |

## Authors

- [@Moheeeetgupta](https://github.com/Moheeeetgupta)
- [@sakshi-123-eng](https://github.com/sakshi-123-eng)
- [@shivi7519](https://github.com/shivi7519)

