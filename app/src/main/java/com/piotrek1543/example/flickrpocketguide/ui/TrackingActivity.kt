package com.piotrek1543.example.flickrpocketguide.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.piotrek1543.example.flickrpocketguide.Constants
import com.piotrek1543.example.flickrpocketguide.R
import com.piotrek1543.example.flickrpocketguide.service.TrackingService
import com.piotrek1543.example.flickrpocketguide.utils.GpsUtils
import kotlinx.android.synthetic.main.activity_main.*


class TrackingActivity : AppCompatActivity() {

    private lateinit var gpsUtils: GpsUtils
    private var menuItem: MenuItem? = null
    private var isGPSEnabled = MutableLiveData<Boolean>()
    private val isRunningData = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ""

        gpsUtils = GpsUtils(this)

        isGPSEnabled.observe(this, Observer {
            if (it) startService() else stopService()
        })
    }

    override fun onResume() {
        super.onResume()
        val serviceFilter = IntentFilter()
        serviceFilter.addAction(ACTION_TRACKING)
        registerReceiver(trackingServiceReceiver, serviceFilter)

        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        registerReceiver(gpsStateChangeReceiver, filter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_track_activity) {
            val isRunning = isRunningData.value ?: false
            when {
                isGPSEnabled.value != true -> gpsUtils.turnGPSOn()
                else -> if (isRunning) stopService() else startService()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        menuItem = menu.findItem(R.id.action_track_activity)

        return true
    }

    override fun onPause() {
        super.onPause()
        //unregisterReceiver(trackingServiceReceiver)
        //unregisterReceiver(gpsStateChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    private fun startService() {
        if (ActivityCompat.checkSelfPermission(
                this@TrackingActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@TrackingActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@TrackingActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                Constants.LOCATION_REQUEST
            )

        } else {
            val serviceIntent = Intent(this, TrackingService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }

    private fun stopService() {
        val serviceIntent = Intent(this, TrackingService::class.java)
        stopService(serviceIntent)
    }

    private val trackingServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val locations = intent.getStringExtra(TrackingService.ARG_LOCATIONS) ?: ""
            text_hello.text = locations

            val isServiceRunning = intent.getBooleanExtra(TrackingService.ARG_IS_RUNNING, true)
            val stringId =
                if (isServiceRunning) R.string.action_track_stop else R.string.action_track_start

            menuItem?.title = resources.getString(stringId)
            isRunningData.value = isServiceRunning
        }
    }

    private val gpsStateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {


            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                // Make an action or refresh an already managed state.

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                if (isGpsEnabled) {
                    Log.i(
                        this.javaClass.name,
                        "gpsStateChangeReceiver.onReceive() location is enabled : isGpsEnabled = $isGpsEnabled"
                    )
                    isGPSEnabled.value = true
                } else {
                    Log.w(this.javaClass.name, "gpsStateChangeReceiver.onReceive() location disabled ")
                    isGPSEnabled.value = false
                }
            }
        }
    }

    companion object {
        const val ACTION_TRACKING = "ACTION_TRACKING"
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGPSEnabled.value = true
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                isGPSEnabled.value = true // flag maintain before get location
            }
        }

    }
}
