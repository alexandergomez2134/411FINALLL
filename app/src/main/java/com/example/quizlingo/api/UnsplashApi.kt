package com.example.quizlingo.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("client_id") clientId: String,
    ): Call<SearchResult>
}

data class SearchResult(
    val results: List<Photo>
)