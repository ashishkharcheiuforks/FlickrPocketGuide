package com.piotrek1543.example.flickrpocketguide.ui.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.piotrek1543.example.flickrpocketguide.FlickrPocketGuideApp
import com.piotrek1543.example.flickrpocketguide.presentation.ViewModelFactory

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>): T {
    val repository = (applicationContext as FlickrPocketGuideApp).taskRepository
    return ViewModelProviders.of(this, ViewModelFactory(repository)).get(viewModelClass)
}
