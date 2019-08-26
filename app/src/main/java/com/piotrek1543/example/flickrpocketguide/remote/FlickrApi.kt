package com.piotrek1543.example.flickrpocketguide.remote

import com.piotrek1543.example.flickrpocketguide.remote.model.PhotosModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/?method=flickr.photos.search&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("api_key") apiKey: String = "7521fa1276b19537eb2bdfdd4b119c5c",
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("per_page") perPage: Int = 5 //get only one image
    ): Response<PhotosModel>

}
