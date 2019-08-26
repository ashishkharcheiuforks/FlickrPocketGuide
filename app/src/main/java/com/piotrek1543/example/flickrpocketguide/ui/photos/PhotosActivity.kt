package com.piotrek1543.example.flickrpocketguide.ui.photos

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
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
import com.piotrek1543.example.flickrpocketguide.presentation.photos.PhotosViewModel
import com.piotrek1543.example.flickrpocketguide.ui.broadcast.ReceiverManager
import com.piotrek1543.example.flickrpocketguide.ui.extensions.obtainViewModel
import com.piotrek1543.example.flickrpocketguide.ui.service.LocationService
import com.piotrek1543.example.flickrpocketguide.ui.utils.GpsUtils
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class PhotosActivity : AppCompatActivity() {

    private lateinit var receiverManager: ReceiverManager
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var adapter: PhotosAdapter
    private lateinit var gpsUtils: GpsUtils
    private var menuItem: MenuItem? = null
    private var isGPSEnabled = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ""

        photosViewModel = obtainViewModel(PhotosViewModel::class.java)
        receiverManager = ReceiverManager.init(this)

        gpsUtils = GpsUtils(this)
        isGPSEnabled.observe(this, Observer {
            if (it) startService() else stopService()
        })

        adapter = PhotosAdapter(context = this@PhotosActivity)
        recycler_locations.adapter = adapter

        photosViewModel.photosLiveData.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        if (isLocationServiceRunning()) {
            when (receiverManager.isReceiverRegistered(trackingServiceReceiver)) {
                true -> {
                    Timber.d("Receiver already registered")
                }
                false -> registerTrackingServiceReceiver()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_track_activity) {
            val isGPSEnabled = isGPSEnabled.value ?: gpsUtils.isProviderEnabled()

            if (!isGPSEnabled) gpsUtils.turnGPSOn()
            else if (isLocationServiceRunning()) stopService() else startService()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        menuItem = menu.findItem(R.id.action_track_activity)
        val resId =
            if (isLocationServiceRunning()) R.string.action_track_stop else R.string.action_track_start
        menuItem?.title = resources.getString(resId)

        return true
    }

    private fun startService() {
        if (ActivityCompat.checkSelfPermission(
                this@PhotosActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@PhotosActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@PhotosActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.LOCATION_REQUEST
            )

        } else {
            registerTrackingServiceReceiver()

            val serviceIntent = Intent(this, LocationService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)

            menuItem?.title = resources.getString(R.string.action_track_stop)
        }
    }

    private fun registerTrackingServiceReceiver() {
        val serviceFilter = IntentFilter()
        serviceFilter.addAction(ACTION_TRACKING)
        receiverManager.registerReceiver(trackingServiceReceiver, serviceFilter)
    }

    private fun stopService() {
        receiverManager.unregisterReceiver(trackingServiceReceiver)

        val serviceIntent = Intent(this, LocationService::class.java)
        stopService(serviceIntent)

        menuItem?.title = resources.getString(R.string.action_track_start)
    }

    private val trackingServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location =
                intent.getParcelableExtra<Location>(LocationService.ARG_LOCATION) ?: return
            photosViewModel.fetchPhotos(lat = location.latitude, lon = location.longitude)
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
                    Toast.makeText(
                        this,
                        getString(R.string.warning_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.GPS_REQUEST) {
            isGPSEnabled.postValue(true) // flag maintain before get location
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService::class.java.name == service.service.className) return true
        }
        return false
    }
}
