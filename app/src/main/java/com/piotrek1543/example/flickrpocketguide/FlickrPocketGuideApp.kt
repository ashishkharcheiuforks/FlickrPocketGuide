package com.piotrek1543.example.flickrpocketguide

import android.app.Application
import com.piotrek1543.example.flickrpocketguide.ui.utils.NotificationUtils
import timber.log.Timber


@Suppress("unused")
class FlickrPocketGuideApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        NotificationUtils.createNotificationChannel(this.applicationContext)
    }
}