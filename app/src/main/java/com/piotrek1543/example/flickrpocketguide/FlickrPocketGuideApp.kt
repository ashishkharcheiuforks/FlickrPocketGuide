package com.piotrek1543.example.flickrpocketguide

import android.app.Application
import com.piotrek1543.example.flickrpocketguide.data.repository.PhotosRepository
import com.piotrek1543.example.flickrpocketguide.ui.utils.NotificationUtils
import timber.log.Timber


@Suppress("unused")
class FlickrPocketGuideApp : Application() {

    val taskRepository: PhotosRepository
        get() = ServiceLocator.providePhotosRepository(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        NotificationUtils.createNotificationChannel(this.applicationContext)
    }
}