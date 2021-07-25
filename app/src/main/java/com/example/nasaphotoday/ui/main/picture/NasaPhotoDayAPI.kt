package com.example.nasaphotoday.ui.main.picture

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaPhotoDayAPI {
    @GET("planetary/apod")
    fun getNasaPhotoDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PODServerResponseData>

}