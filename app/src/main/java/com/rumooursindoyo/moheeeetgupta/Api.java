package com.rumooursindoyo.moheeeetgupta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *  Retrofit in android :- it is type-safe REST client for Android and Java which aims to make it easier to consume RESTful web services.
 * Retrofit automatically serialises the JSON response using a POJO(Plain Old Java Object) which must be defined in advanced for
 * the JSON Structure.
 *  It manages the process of receiving, sending, and creating HTTP requests and responses.
 *  It alternates IP addresses if there is a connection to a web service failure.
 */


/**
 * Endpoints usually are defined inside an Interface class. An endpoint refers to the path where information is obtained.
 * Our endpoint is 'commentThreads\’. Since our aim is to obtain information from the API, we will be using the @GET annotation
 * since we are making a Get request.
 *
 * Next we will have a Call<Results> object that will return the information from the API.
 *
 */
// this is a java interface
public interface Api {




    // base url for queuring data
    String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    @GET("commentThreads/")

    /**
     * @Query :- Retrofit uses @Query annotation to define query parameters for requests. Query parameters are defined before method parameters.
     * In annotation, we pass the query parameter name which will be appended in the URL.
     */
    Call<Results> getsuperHeroes(@Query( "key" ) String key,
                                 @Query( "textFormat" ) String format,
                                 @Query( "part" ) String part,
                                 @Query( "maxResults" ) Integer maxResults,
                                 @Query( "videoId" ) String vid);

}
