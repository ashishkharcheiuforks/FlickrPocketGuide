package com.piotrek1543.example.flickrpocketguide.cache.dao

import androidx.room.*
import com.piotrek1543.example.flickrpocketguide.cache.model.CachedPhoto

/**
 * Data Access Object for the photos table.
 */
@Dao
interface CachedPhotosDao {

    /**
     * Select all photos from the photos table.
     *
     * @return all photos.
     */
    @Query("SELECT * FROM photos")
    suspend fun getPhotos(): List<CachedPhoto>

    /**
     * Select a photoModel by id.
     *
     * @param photoId the photoModel id.
     * @return the photoModel with photoId.
     */
    @Query("SELECT * FROM Photos WHERE id = :photoId")
    suspend fun getPhotoById(photoId: String): CachedPhoto?

    /**
     * Insert a cachedPhoto in the database. If the cachedPhoto already exists, replace it.
     *
     * @param cachedPhoto the cachedPhoto to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(cachedPhoto: CachedPhoto)

    /**
     * Update a cachedPhoto.
     *
     * @param cachedPhoto cachedPhoto to be updated
     * @return the number of photos updated. This should always be 1.
     */
    @Update
    suspend fun updatePhoto(cachedPhoto: CachedPhoto): Int

    /**
     * Delete all photos.
     */
    @Query("DELETE FROM Photos")
    suspend fun deletePhotos()
}