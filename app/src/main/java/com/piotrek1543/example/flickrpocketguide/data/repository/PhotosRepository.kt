package com.piotrek1543.example.flickrpocketguide.data.repository

import com.piotrek1543.example.flickrpocketguide.cache.PhotosLocalDataSource
import com.piotrek1543.example.flickrpocketguide.data.Result
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.PhotosRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PhotosRepository(
    private val remoteDataSource: PhotosRemoteDataSource,
    private val localDataSource: PhotosLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository() {

    suspend fun getPhotos(lat: Double, lon: Double): Result<List<PhotoEntity>> {
        return withContext(ioDispatcher) {
            val remoteResult = remoteDataSource.getPhotosByLocation(lat = lat, lon = lon)
            val localResult = localDataSource.getPhotos()

            if (remoteResult is Result.Success) {
                if (localResult is Result.Success) {
                    val photo = remoteResult.data.firstOrNull { entity: PhotoEntity ->
                        !localResult.data.map { it.id }.contains(entity.id)
                    }
                    photo?.let { localDataSource.savePhoto(it) }

                    return@withContext localDataSource.getPhotos()
                } else {
                    return@withContext localResult
                }
            } else {
                Timber.d("")
                return@withContext remoteResult
            }
        }
    }
}