package com.piotrek1543.example.flickrpocketguide.data.repository

import com.piotrek1543.example.flickrpocketguide.data.remote.FlickrApi
import com.piotrek1543.example.flickrpocketguide.model.Photo

class PhotosRepository(private val api: FlickrApi) : BaseRepository() {

    suspend fun getPhotos(lat: Double, lon: Double): MutableList<Photo>? {

        val response = safeApiCall(
            call = { api.search(lat = lat, lon = lon) },
            errorMessage = "Error Fetching Photos"
        )

        return response?.photos?.photo?.toMutableList()

    }

}