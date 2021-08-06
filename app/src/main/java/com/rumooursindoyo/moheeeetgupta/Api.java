package com.rumooursindoyo.moheeeetgupta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {




    String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    @GET("commentThreads/")
    Call<Results> getsuperHeroes(@Query( "key" ) String key,
                                 @Query( "textFormat" ) String format,
                                 @Query( "part" ) String part,
                                 @Query( "maxResults" ) Integer maxResults,
                                 @Query( "videoId" ) String vid);

}
