package com.piotrek1543.example.flickrpocketguide.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.piotrek1543.example.flickrpocketguide.R
import com.piotrek1543.example.flickrpocketguide.service.TrackingService
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class TrackingActivity : AppCompatActivity() {

    private var menuItem : MenuItem? = null
    private val isRunningData = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ""

        val filter = IntentFilter()
        filter.addAction(ACTION_TRACKING)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val millis = intent.getLongExtra(TrackingService.ARG_TIMER, 0)
                text_hello.text = millis.toString()

                val isServiceRunning = intent.getBooleanExtra(TrackingService.ARG_IS_RUNNING, true)
                val stringId = if (isServiceRunning) R.string.action_track_stop else R.string.action_track_start

                menuItem?.title = resources.getString(stringId)
                isRunningData.value = isServiceRunning

                Timber.d("sssssssss $isServiceRunning")

            }
        }

        registerReceiver(receiver, filter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_track_activity) {
            val isRunning = isRunningData.value ?: false
            if (isRunning) stopService() else startService()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        menuItem = menu.findItem(R.id.action_track_activity)

        return true
    }

    private fun startService() {
        val serviceIntent = Intent(this, TrackingService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopService() {
        val serviceIntent = Intent(this, TrackingService::class.java)
        stopService(serviceIntent)
    }

    companion object {
        const val ACTION_TRACKING = "ACTION_TRACKING"
    }
}
