package com.piotrek1543.example.flickrpocketguide.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.piotrek1543.example.flickrpocketguide.cache.dao.CachedPhotosDao
import com.piotrek1543.example.flickrpocketguide.cache.model.CachedPhoto
import com.piotrek1543.example.flickrpocketguide.remote.model.PhotoModel

/**
 * The Room Database that contains the Task table.
 */
@Database(entities = [CachedPhoto::class], version = 1, exportSchema = false)
abstract class PhotosDatabase : RoomDatabase() {

    abstract fun photosDao(): CachedPhotosDao
}