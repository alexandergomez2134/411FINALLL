package com.example.quizlingo.api

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "UnsplashExecutor"

class UnsplashExecutor(private val imageView: ImageView) {
    private val api: UnsplashApi

    init {
        // Create a Retrofit instance to make API requests
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create an instance of the UnsplashApi interface
        this.api = retrofit.create(UnsplashApi::class.java)
    }

    fun searchPhotos(query: String) {
        // Call the searchPhotos method on the UnsplashApi interface to get search results
        val clientId = "IUBzaTo1Z3ItDHcMBjEt4XnYZp4Yi-DOignqYULIOiI"
        val call = this.api.searchPhotos(query, clientId)

        // Handle the response from the API
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    val searchResult = response.body()
                    if (searchResult?.results?.isNotEmpty() == true) {
                        // Load the first photo from the search results into the ImageView
                        val photo = searchResult.results[1]
                        val imageUrl = photo.urls.regular
                        // Use Picasso to load the image into the ImageView
                        Picasso.get()
                            .load(imageUrl)
                            .into(imageView)
                        Log.d(TAG, "First image URL: $imageUrl")

                    } else {
                        // Handle case where there are no search results
                        Log.d(TAG, "No search results returned.")
                    }
                } else {
                    // Handle case where the API request was not successful
                    Log.d(TAG, "API request was not successful.")
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                // Handle case where the API request failed
                Log.e(TAG, "API request failed: ${t.message}")
            }
        })
    }
}