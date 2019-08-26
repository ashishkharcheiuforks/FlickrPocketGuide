package com.piotrek1543.example.flickrpocketguide

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.piotrek1543.example.flickrpocketguide.cache.PhotosDatabase
import com.piotrek1543.example.flickrpocketguide.cache.PhotosLocalDataSource
import com.piotrek1543.example.flickrpocketguide.data.repository.PhotosRepository
import com.piotrek1543.example.flickrpocketguide.remote.PhotosRemoteDataSource

/**
 * A Service Locator for the [PhotosRepository]. This is the prod version, with a
 * the "real" [PhotosRemoteDataSource].
 */
object ServiceLocator {

    private var database: PhotosDatabase? = null
    @Volatile
    var photosRepository: PhotosRepository? = null
        @VisibleForTesting set

    fun providePhotosRepository(context: Context): PhotosRepository {
        synchronized(this) {
            return photosRepository ?: photosRepository ?: createPhotosRepository(context)
        }
    }

    private fun createPhotosRepository(context: Context): PhotosRepository {
        database = Room.databaseBuilder(
            context.applicationContext,
            PhotosDatabase::class.java, "Photos.db"
        )
            .build()

        return PhotosRepository(
            PhotosRemoteDataSource(),
            PhotosLocalDataSource(database!!.photosDao())
        )
    }
}