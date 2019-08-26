package com.piotrek1543.example.flickrpocketguide.presentation.photos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.piotrek1543.example.flickrpocketguide.data.Result
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.data.repository.PhotosRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PhotosViewModel(private val repository: PhotosRepository) : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val viewModelScope = CoroutineScope(coroutineContext)

    val photosLiveData = MutableLiveData<List<PhotoEntity>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun fetchPhotos(lat: Double = 52.40692, lon: Double = 16.92993) {
        viewModelScope.launch {
            repository.getPhotos(lat = lat, lon = lon).let { result ->
                when (result) {
                    is Result.Success<List<PhotoEntity>> ->
                        photosLiveData.postValue(result.data)
                    is Result.Error ->
                        errorLiveData.postValue(result.exception)
                }
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}