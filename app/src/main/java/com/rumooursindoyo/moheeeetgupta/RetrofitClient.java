package com.rumooursindoyo.moheeeetgupta;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit GSON convertor :- Retrofit is a very popular HTTP client library for Android.
 * Using Retrofit makes it easy to parse API response and use it in your application. I
 * t has built-in GSON converter that can automatically parse HTTP response into an Object or any other types in Java that can
 * be used in your code.
 *
 * Google's Gson library provides a powerful framework for converting between JSON strings and Java objects.
 * This library helps to avoid needing to write boilerplate code to parse JSON responses yourself.
 * It can be used with any networking library, including the Android Async HTTP Client and OkHttp.
 */

/**
 * difference between JSON and Gson :-
 * GSON is a java API from Google that converts java objects to their JSON representations and vice-versa
 */



/**
 * This Java class is used to send requests to an API.
 * We specify the URL that contains the data required and use the Retrofit Builder class.
 */
public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Api myApi;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                // If your application is Restful, so gets and sends data from / to server.
                // Converter factory need to be added, just for retrofit can convert JSON data (got from server) into
                // java (model) objects (POJO), to use in Android Project.
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}