package com.piotrek1543.example.flickrpocketguide.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.piotrek1543.example.flickrpocketguide.R
import com.piotrek1543.example.flickrpocketguide.ui.TrackingActivity

object NotificationUtils {

    internal fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                TRACKING_CHANNEL_ID,
                "Tracking Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    internal fun createNotification(context: Context): Notification? {
        val notificationIntent = Intent(context, TrackingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        val resources = context.resources

        return NotificationCompat.Builder(context, TRACKING_CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.currently_tracking_activity))
            .setContentText(resources.getString(R.string.tap_to_see_details))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setShowWhen(false)
            .build()
    }

    private const val TRACKING_CHANNEL_ID = "trackingChannelId"

}