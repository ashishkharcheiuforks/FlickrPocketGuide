package com.piotrek1543.example.flickrpocketguide.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piotrek1543.example.flickrpocketguide.data.repository.PhotosRepository
import com.piotrek1543.example.flickrpocketguide.presentation.photos.PhotosViewModel

class ViewModelFactory constructor(
    private val photosRepository: PhotosRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(PhotosViewModel::class.java) ->
                    PhotosViewModel(photosRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}