package com.piotrek1543.example.flickrpocketguide.data.repository

import com.piotrek1543.example.flickrpocketguide.cache.mapper.CachedPhotoEntityMapper
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.FlickrApi
import com.piotrek1543.example.flickrpocketguide.remote.mapper.RemotePhotoEntityMapper

class PhotosRepository(
    private val api: FlickrApi,
    val cachedMapper: CachedPhotoEntityMapper = CachedPhotoEntityMapper(),
    val remoteMapper: RemotePhotoEntityMapper = RemotePhotoEntityMapper()
) : BaseRepository() {

    suspend fun getPhotos(lat: Double, lon: Double): MutableList<PhotoEntity>? {

        val response = safeApiCall(
            call = { api.search(lat = lat, lon = lon) },
            errorMessage = "Error Fetching Photos"
        )
        val list = response?.photos?.photo

        return list?.map { remoteMapper.mapFromRemote(it) }?.toMutableList()

    }

}