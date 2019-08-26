package com.piotrek1543.example.flickrpocketguide.ui.photos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.ApiProvider
import com.piotrek1543.example.flickrpocketguide.data.repository.PhotosRepository
import com.piotrek1543.example.flickrpocketguide.remote.model.PhotoModel
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class PhotosViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: PhotosRepository = PhotosRepository(ApiProvider.flickrApi)

    val photosLiveData = MutableLiveData<MutableList<PhotoEntity>>().apply { value = arrayListOf() }

    fun fetchPhotos(lat: Double = 52.40692, lon: Double = 16.92993) {
        scope.launch {
            val photos = repository.getPhotos(lat = lat, lon = lon) ?: return@launch
            val list = photosLiveData.value!!
            photosLiveData.postValue(photos)

            Timber.d("photossssss ${list.count()}")

        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}