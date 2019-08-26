package com.piotrek1543.example.flickrpocketguide.data

import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity

/**
 * Main entry point for accessing photos data.
 *
 *
 * For simplicity, only getPhotos() and getPhoto() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new photo is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
interface PhotosDataSource {

    suspend fun getPhotosByLocation(lat: Double, lon: Double): Result<List<PhotoEntity>>

    suspend fun getPhotos(): Result<List<PhotoEntity>>

    suspend fun getPhoto(photoId: String): Result<PhotoEntity>

    suspend fun savePhoto(photo: PhotoEntity)

    suspend fun savePhotos(list: List<PhotoEntity>)

    suspend fun deleteAllPhotos()
}
