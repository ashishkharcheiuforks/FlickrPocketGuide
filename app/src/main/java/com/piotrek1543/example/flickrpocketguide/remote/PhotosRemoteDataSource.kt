package com.piotrek1543.example.flickrpocketguide.remote

import com.piotrek1543.example.flickrpocketguide.data.PhotosDataSource
import com.piotrek1543.example.flickrpocketguide.data.Result
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.mapper.RemotePhotoEntityMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PhotosRemoteDataSource internal constructor(
    private val api: FlickrApi = ApiProvider.flickrApi,
    private val mapper: RemotePhotoEntityMapper = RemotePhotoEntityMapper(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotosDataSource {

    override suspend fun getPhotos(): Result<List<PhotoEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPhoto(photoId: String): Result<PhotoEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun savePhoto(photo: PhotoEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun savePhotos(list: List<PhotoEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllPhotos() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPhotosByLocation(lat: Double, lon: Double): Result<List<PhotoEntity>> =
        withContext(ioDispatcher) {
            try {

                val call = api.search(lat = lat, lon = lon)

                if (call.isSuccessful) {
                    val list = call.body()?.photos?.photo
                    return@withContext Result.Success(list?.map {
                        mapper.mapFromRemote(it)
                    } ?: emptyList())
                } else {
                    Timber.e(call.message())
                    return@withContext Result.Error(Exception(call.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

}