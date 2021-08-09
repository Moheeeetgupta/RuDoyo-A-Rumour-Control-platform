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
- Connect to your self-hosted wallabag instance or connect to your [wallabag.it](https://wallabag.it) or [Framabag](https://framabag.org) account.
- Supports wallabag 2.0 and higher.
- Completely ad-free.
- Increase and decrease the size of the font and also switch between a serif or sans-serif font for a more comfortable reading experience.
- Switch between numerous themes.
- Possibility to cache images locally for offline reading.
- Get articles read via Text-to-Speech feature.
- Needs no special permissions on Android 6.0+.

## Screenshots

[<img src="/readme/Wallabag%20Reading%20List.png" align="left"
width="200"
    hspace="10" vspace="10">](/readme/Wallabag%20Reading%20List.png)
[<img src="/readme/Wallabag%20Article%20View.png" align="center"
width="200"
    hspace="10" vspace="10">](/readme/Wallabag%20Article%20View.png)

## Permissions

On Android versions prior to Android 6.0, wallabag requires the following permissions:
- Full Network Access.
- View Network Connections.
- Run at startup.
- Read and write access to external storage.

The "Run at startup" permission is only used if Auto-Sync feature is enabled and is not utilised otherwise. The network access permissions are made use of for downloading content. The external storage permission is used to cache article images for viewing offline.

## Contributing

wallabag app is a free and open source project developed by volunteers. Any contributions are welcome. Here are a few ways you can help:
 * [Report bugs and make suggestions.](https://github.com/wallabag/android-app/issues)
 * [Translate the app](https://hosted.weblate.org/projects/wallabag/android-app/) (you don't have to create an account).
 * Write some code. Please follow the code style used in the project to make a review process faster.

## License

This application is released under GNU GPLv3 (see [LICENSE](LICENSE)).
Some of the used libraries are released under different licenses.
