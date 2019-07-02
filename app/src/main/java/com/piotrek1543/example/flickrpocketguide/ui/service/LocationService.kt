package com.piotrek1543.example.flickrpocketguide.ui.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.annotation.Nullable
import com.google.android.gms.location.*
import com.piotrek1543.example.flickrpocketguide.ui.photos.PhotosActivity
import com.piotrek1543.example.flickrpocketguide.ui.utils.NotificationUtils


class LocationService : Service() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private var results = FloatArray(2)

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = NotificationUtils.createNotification(this.applicationContext)
        startForeground(1, notification)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (2 * 1000).toLong() // 2 seconds
        locationRequest.fastestInterval = (1 * 1000).toLong() // 1 seconds

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) return

                for (location in locationResult.locations) {
                    if (location == null) return
                    Location.distanceBetween(wayLatitude, wayLongitude, location.latitude, location.longitude, results)
                    if (results[0] >= 100) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        broadcastLocation(location)
                    }
                }
            }
        }

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun broadcastLocation(location: Location) {
        val intent = Intent(PhotosActivity.ACTION_TRACKING)
        intent.putExtra(ARG_LOCATION, location)
        sendBroadcast(intent)
    }

    companion object {
        const val ARG_LOCATION = "location"
    }

}