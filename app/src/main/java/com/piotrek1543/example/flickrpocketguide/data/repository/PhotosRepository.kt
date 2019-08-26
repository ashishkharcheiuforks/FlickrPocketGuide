package com.piotrek1543.example.flickrpocketguide.data.repository

import com.piotrek1543.example.flickrpocketguide.cache.PhotosLocalDataSource
import com.piotrek1543.example.flickrpocketguide.cache.mapper.CachedPhotoEntityMapper
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.FlickrApi
import com.piotrek1543.example.flickrpocketguide.remote.PhotosRemoteDataSource
import com.piotrek1543.example.flickrpocketguide.remote.mapper.RemotePhotoEntityMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.piotrek1543.example.flickrpocketguide.data.Result

class PhotosRepository(
    private val remoteDataSource: PhotosRemoteDataSource,
    private val localDataSource: PhotosLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository() {

    suspend fun getPhotos(lat: Double, lon: Double): Result<List<PhotoEntity>> {
        return withContext(ioDispatcher) {
            remoteDataSource.getPhotosByLocation(lat = lat, lon = lon)
        }

    }

}