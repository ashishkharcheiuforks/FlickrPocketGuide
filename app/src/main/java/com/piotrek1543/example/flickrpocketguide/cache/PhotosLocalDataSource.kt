package com.piotrek1543.example.flickrpocketguide.cache

import com.piotrek1543.example.flickrpocketguide.cache.dao.CachedPhotosDao
import com.piotrek1543.example.flickrpocketguide.cache.mapper.CachedPhotoEntityMapper
import com.piotrek1543.example.flickrpocketguide.data.PhotosDataSource
import com.piotrek1543.example.flickrpocketguide.data.Result
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosLocalDataSource internal constructor(
    private val photosDao: CachedPhotosDao,
    private val mapper: CachedPhotoEntityMapper = CachedPhotoEntityMapper(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotosDataSource {

    override suspend fun getPhotosByLocation(lat: Double, lon: Double): Result<List<PhotoEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPhoto(photoId: String): Result<PhotoEntity> = withContext(ioDispatcher) {
        try {
            val photo = photosDao.getPhotoById(photoId)
            if (photo != null) {
                return@withContext Result.Success(mapper.mapFromCached(photo))
            } else {
                return@withContext Result.Error(Exception("Photo not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun savePhoto(photo: PhotoEntity) = withContext(ioDispatcher) {
        photosDao.insertPhoto(mapper.mapToCached(photo))
    }

    override suspend fun savePhotos(list: List<PhotoEntity>) = withContext(ioDispatcher) {
        photosDao.insertPhotos(list.map { mapper.mapToCached(it) })
    }

    override suspend fun deleteAllPhotos() = withContext(ioDispatcher) {
        photosDao.deletePhotos()
    }

    override suspend fun getPhotos(): Result<List<PhotoEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            val photos = photosDao.getPhotos()
            Result.Success(photos.map { mapper.mapFromCached(it) })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}