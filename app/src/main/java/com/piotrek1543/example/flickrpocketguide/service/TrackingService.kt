package com.piotrek1543.example.flickrpocketguide.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import com.google.android.gms.location.*
import com.piotrek1543.example.flickrpocketguide.ui.TrackingActivity
import com.piotrek1543.example.flickrpocketguide.utils.NotificationUtils


class TrackingService : Service() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val locations = arrayListOf<String>()
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = NotificationUtils.createNotification(this.applicationContext)
        startForeground(1, notification)
        broadcastStatus(true)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (2 * 1000).toLong() // 2 seconds
        locationRequest.fastestInterval = (1 * 1000).toLong() // 1 seconds

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        locations.add("$wayLatitude $wayLongitude")
                        broadcastLocation(locations)
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
        broadcastStatus(false)
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        broadcastStatus(false)
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun broadcastStatus(isRunning: Boolean) {
        val intent = Intent(TrackingActivity.ACTION_TRACKING)
        intent.putExtra(ARG_IS_RUNNING, isRunning)
        sendBroadcast(intent)
    }

    private fun broadcastLocation(locations: ArrayList<String>) {
        val i = Intent(TrackingActivity.ACTION_TRACKING)
        i.putExtra(ARG_LOCATIONS, locations)
        sendBroadcast(i)
    }

    companion object {
        const val ARG_LOCATIONS = "locations"
        const val ARG_IS_RUNNING = "isRunning"
    }

}